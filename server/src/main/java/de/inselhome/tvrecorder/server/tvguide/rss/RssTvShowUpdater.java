/**
 * Copyright (C) 2011 Ingo Weinzierl (ingo_weinzierl@web.de)
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses/>.
 *
 */
package de.inselhome.tvrecorder.server.tvguide.rss;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.log4j.Logger;

import de.inselhome.tvrecorder.common.objects.Channel;
import de.inselhome.tvrecorder.common.objects.ChannelWithTvGuide;
import de.inselhome.tvrecorder.common.objects.TvShow;

import de.inselhome.tvrecorder.server.backend.Backend;
import de.inselhome.tvrecorder.server.rss.RssFeed;
import de.inselhome.tvrecorder.server.rss.RssItem;
import de.inselhome.tvrecorder.server.rss.RssReader;
import de.inselhome.tvrecorder.server.tvguide.TvShowUpdateEvent;
import de.inselhome.tvrecorder.server.tvguide.TvShowUpdateListener;
import de.inselhome.tvrecorder.server.tvguide.TvShowUpdater;


/**
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class RssTvShowUpdater implements TvShowUpdater {

    private static Logger logger = Logger.getLogger(RssTvShowUpdater.class);


    /** The default interval that is used to update the tv shows.*/
    public static final long DEFAULT_INTERVAL = 1000 * 60 * 60;

    public static final int MAX_THREAD_COUNT = 1;


    protected Backend backend;

    protected Stack<Job> jobs;

    protected Thread waiting;

    protected int active;

    protected List<ChannelWithTvGuide> updated;

    protected List<TvShowUpdateListener> listeners;


    public RssTvShowUpdater(Backend backend) {
        this.backend   = backend;
        this.jobs      = new Stack<Job>();
        this.updated   = new ArrayList<ChannelWithTvGuide>();
        this.listeners = new ArrayList<TvShowUpdateListener>();
        this.active    = 0;
    }


    /**
     * This method starts the RssTvShowUpdater.
     */
    public void start() {
        logger.info("Started the RssTvShowUpdater...");

        Thread updater = new Thread("RssTvShowUpdater-Thread") {
            public void run() {
                runUpdate();
            }
        };
        updater.start();
    }


    /**
     * Registers a new TvShowUpdateListener.
     *
     * @param listener A new listener that wants to listen to tv guide changes.
     */
    public void addTvShowUpdateListener(TvShowUpdateListener listener) {
        listeners.add(listener);
    }


    protected synchronized void increaseActive() {
        active = active + 1;
    }


    protected synchronized void decreaseActive() {
        active = active == 1 ? 0 : active - 1;
    }


    protected synchronized Job getNextJob() {
        return jobs.isEmpty() ? null : jobs.pop();
    }


    protected synchronized void addUpdated(ChannelWithTvGuide update) {
        updated.add(update);
    }


    protected void runUpdate() {
        do {
            // Remove old channels first
            updated.clear();

            prepareJobs();
            updateTvShows();

            try {
                Thread.sleep(DEFAULT_INTERVAL);
            }
            catch (InterruptedException ie) {
                logger.warn(ie, ie);
            }
        }
        while (true);
    }


    protected void prepareJobs() {
        // clear old jobs first although this should not be necessary
        synchronized (jobs) {
            jobs.clear();

            for (String[] part: RssReader.CHANNELS) {
                String tmp = RssReader.BASE_URL + part[0];

                try {
                    URL     url = new URL(tmp);
                    Channel c   = new Channel(part[0], part[1]);

                    if (url != null) {
                        logger.debug("Add job: " + url);
                        jobs.push(new Job(c, url));
                    }
                }
                catch (MalformedURLException mue) {
                    logger.warn(mue, mue);
                }
            }
        }
    }


    /**
     * This method starts a number of worker threads to fetch tv show
     * information.
     */
    protected void updateTvShows() {
        logger.info("++ RssTvShowUpdater starts update process now ++");

        waiting = Thread.currentThread();

        for (int i = 0; i < MAX_THREAD_COUNT; i++) {
            Thread t = new Thread("RssTvShowUpdateHelper-" + i) {
                public void run() {
                    doUpdate(getName());
                }
            };
            t.start();
        }

        boolean finished = false;
        do {
            try {
                Thread.sleep(Long.MAX_VALUE);

                logger.debug("Wake up.");
            }
            catch (InterruptedException ie) {
                // do nothing
            }

            synchronized (this) {
                finished = (jobs.isEmpty() && active <= 0);
            }
        }
        while (!finished);

        fireTvShowsChanged(updated);
    }


    /**
     * This method is used by the threads to update the TV shows of a specific
     * channel.
     *
     * @param name The name of the worker thread - which is used for logging
     * only.
     */
    protected void doUpdate(String name) {
        logger.debug("Run update with " + name);

        // increase number of running threads
        increaseActive();

        Job job = getNextJob();

        while (job != null) {
            logger.debug(name + " process: " + job.url);

            RssReader reader = new RssReader(job.url);
            RssFeed   feed   = reader.getFeed();

            ChannelWithTvGuide cwtv = new ChannelWithTvGuide(job.c);

            int num = feed.getItemSize();

            for (int i = 0; i < num; i++) {
                RssItem item = feed.getItem(i);

                if (item != null) {
                    cwtv.addTvShow(
                        new TvShow(
                            item.getTitle(),
                            item.getDescription(),
                            item.getStart()));
                }
            }

            addUpdated(cwtv);

            try {
                // XXX This is a work around: there seems to be problems when
                // running mutliple or a single threads without sleeping. The
                // rss feeds fetched from server are all the same. After this
                // wait, this problem doesn't occur anymore. But that problem
                // has to be fixed!
                Thread.currentThread().sleep(10000);
            }
            catch (InterruptedException ie) { /* do nothing here */ }

            synchronized (jobs) {
                job = !jobs.isEmpty() ? jobs.pop() : null;
            }
        }

        // decrease number of running threads, because we have no jobs left
        decreaseActive();

        logger.debug(name + " has finished work.");

        waiting.interrupt();
    }


    /**
     * This method is used to inform all registered listeners that TvShows
     * changed.
     *
     * @param channels The channels with the new information.
     */
    protected void fireTvShowsChanged(List<ChannelWithTvGuide> channels) {
        logger.debug("Inform TvShowUpdateListener that channels have changed.");

        for (TvShowUpdateListener listener: listeners) {
            listener.onTvShowUpdate(new TvShowUpdateEvent(channels));
        }
    }


    /**
     * A Job is a wrapper for urls and channels.
     */
    private class Job {
        public Channel c;
        public URL     url;

        public Job(Channel c, URL url) {
            this.c   = c;
            this.url = url;
        }
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
