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

from datetime import datetime as dt

from model import Program
from epgparser import EpgParser


class OtrParser(EpgParser):

    def __init__(self, exporter):
        self.exporter = exporter


    def parse(self, doc):
        items = doc.findall('/channel/item')

        if items:
            print "Found %i programs in EPG data." % len(items)
        else:
            print "No programs found in EPG data!"
            return

        for item in items:
            self.parseItem(item)


    def parseItem(self, item):
        channel = item.findtext('sender')

        title    = item.findtext('titel')
        desc     = item.findtext('text')
        start    = float(item.findtext('beginn'))
        end      = float(item.findtext('ende'))
        category = item.findtext('typ')
        length   = int(item.findtext('dauer'))

        if type(title) is unicode:
            title = title.encode('utf-8')

        if type(desc) is unicode:
            desc = desc.encode('utf-8')

        if type(category) is unicode:
            category = category.encode('utf-8')

        program = Program(
            title,
            desc,
            dt.fromtimestamp(start),
            dt.fromtimestamp(end),
            category,
            length)

        self.exporter.add_program(channel, program)

