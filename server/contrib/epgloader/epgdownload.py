#!/usr/bin/env python
#
# Copyright (C) 2011 by Ingo Weinzierl
# Authors: Ingo Weinzierl <ingo_weinzierl@web.de>
#
# This program is free software; you can redistribute it and/or modify it under
# the terms of the GNU General Public License as published by the Free Software
# Foundation; either version 3 of the License, or (at your option) any later
# version.
#
# This program is distributed in the hope that it will be useful, but WITHOUT
# ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
# FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
# details.
#
# You should have received a copy of the GNU General Public License along with
# this program; if not, see <http://www.gnu.org/licenses/>.

import urllib2

from optparse import OptionParser
from lxml.html.soupparser import parse

from otr.otrparser import OtrParser
from xmltv.xmltvexporter import XmltvExporter


__TVGUIDE_URL__ = 'http://www.onlinetvrecorder.com/index.php'
__STATIONS__    = "ARD,ZDF,RTL,SAT1,PRO7,RTL2,SRTL,KABEL1,VOX"
__SRC_FORMAT__  = 'xml'
__DST_FORMAT__  = 'xmltv'


def main():
    (options, args) = parseOpts()

    print "Fetch EPG data from '%s'." % get_url(options)
    raw     = urllib2.urlopen(get_url(options), 'utf-8')
    content = parse(raw)

    exporter = XmltvExporter(options.output)
    parser   = OtrParser(exporter)

    parser.parse(content)
    exporter.write()


def parseOpts():
    parser = OptionParser()
    parser.add_option(
        "-o", "--output",
        dest="output", help="Destination xmltv file path")
    parser.add_option(
        "-s", "--start",
        dest="start", help="Start time for fetching EPG data")
    parser.add_option(
        "-e", "--end",
        dest="end", help="Stop time for fetching EPG data")

    return parser.parse_args()


def get_url(opts):
    static_str = 'aktion=epg_export&btn_ok=OK'
    return "%s?%s&format=%s&stations=%s&from=%s&to=%s" % (
        get_base_url(opts),
        static_str,
        get_format(opts),
        get_stations(opts),
        get_start_date(opts),
        get_stop_date(opts))


def get_base_url(opts):
    return __TVGUIDE_URL__


def get_format(opts):
    return __SRC_FORMAT__


def get_stations(opts):
    return __STATIONS__


def get_start_date(opts):
    return opts.start


def get_stop_date(opts):
    return opts.end


if __name__ == "__main__":
    main()
