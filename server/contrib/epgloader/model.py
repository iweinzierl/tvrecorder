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

class Program:

    def __init__(self, title, description, start, end, category='', length=-1):
        self.title       = title
        self.description = description
        self.start       = start
        self.end         = end
        self.category    = category
        self.length      = length

    def __str__(self):
        return "|%s,%s,%s,%s|" % (
               self.title, self.description, self.start, self.end)

