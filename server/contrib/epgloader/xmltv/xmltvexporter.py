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

import sys
from lxml       import etree
from lxml.etree import tostring
from datetime   import datetime

from exporter import Exporter


__TIMEFORMAT__ = '%Y%m%d%H%M%S'


class XmltvExporter(Exporter):

    def __init__(self, file_path):
        super(Exporter, self).__init__()
        self.file_path = file_path
        self.programs  = {}


    def add_channel(self, channel):
        if not channel in self.programs.keys():
            print "Add new channel: %s" % channel
            self.programs[channel] = []


    def add_program(self, channel, program):
        self.add_channel(channel)
        self.programs[channel].append(program)


    def write(self):
        print "XmltvExporter.write()"
        tv = etree.Element('tv')

        for key in self.programs.keys():
            channel = etree.Element('channel', id=key)
            display = etree.Element('display-name')
            display.text = key

            channel.append(display)
            tv.append(channel)

            for p in self.programs[key]:
                programme = etree.Element(
                    'programme',
                    channel=key,
                    start="%s %s" % (p.start.strftime(__TIMEFORMAT__), "+0100"),
                    stop="%s %s" % (p.end.strftime(__TIMEFORMAT__), "+0100"))

                title = etree.Element('title')
                title.text = p.title

                desc = etree.Element('desc')
                desc.text = p.description.decode('utf-8')

                category = etree.Element('category')
                category.text = p.category.decode('utf-8')

                length = etree.Element('length')
                length.text = str(p.length)

                programme.append(title)
                programme.append(desc)
                programme.append(category)
                programme.append(length)

                tv.append(programme)

        f = open(self.file_path, 'w')
        f.write(tostring(tv, pretty_print=True))

