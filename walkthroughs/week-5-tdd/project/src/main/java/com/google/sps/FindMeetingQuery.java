// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

// Figure out what everything is 
// Find events for attendies, to show conflicts
// Sort by start time
// Search for conflicts
// TODO: Find times where attendies can meet without conflict (Time ranges for mettings without conflict)

package com.google.sps;

import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public final class FindMeetingQuery {
  // @param Event (Collection) - All the events existing on an organization calendar: name, time range, attendies...

  // @param Meeting request - Meeting of a specific time with certain attendies (and duration)

  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {

      // Collection of required attendees
      // Collection of optional attendees

      // COllection of required attendee times
      // Collection of optional attendee times (findAttendeeEventTimes())
      // Possibly all attendees -- times

      // Collection of available times (required/optional)

      // if/else available for all or available for required 

      //for loop?
      //Multiple methods... 

      // Find who need to be at meetings...
      // Find what meetings that are scheduled
      // Find times
      // If space in between meetings is >= duration of meeting... 
      // add to a list of those time frames

      // Returns a collection of time ranges

      ArrayList<TimeRange> eventsForRequiredAttendees = findAttendeeEventTimes(events, request.getAttendees());
      ArrayList<TimeRange> eventsForOptionalAttendees = findAttendeeEventTimes(events, request.getOptionalAttendees());

      // ArrayList of all assigned and optional events
      ArrayList<TimeRange> assignedAndOptionalEvents = new ArrayList();
      assignedAndOptionalEvents.addAll(eventsForRequiredAttendees);
      assignedAndOptionalEvents.addAll(eventsForOptionalAttendees);

      // Sorting the actual events by start time
      Collections.sort(eventsForRequiredAttendees, TimeRange.ORDER_BY_START);
      Collections.sort(eventsForOptionalAttendees, TimeRange.ORDER_BY_START);
      Collections.sort(assignedAndOptionalEvents, TimeRange.ORDER_BY_START);

      // Return possible meeting times for required attendees
      ArrayList<TimeRange> possibleMeetingTimes = 
        findAppropriateTimes(assignedAndOptionalEvents, request.getDuration());

      return possibleMeetingTimes;
  }

  // Lets get the times for events that attendees are going to.. 
  private ArrayList<TimeRange> findAttendeeEventTimes(Collection<Event> events, Collection<String> attendees) {
      ArrayList<TimeRange> attendeeTimes = new ArrayList();

      // Looping over events and people in those events...      
      for (Event event : events) {
          for (String person : attendees) {
              if (event.getAttendees().contains(person)) {
                  attendeeTimes.add(event.getWhen());
                  break;
              }
          }
      }
     return attendeeTimes;
  }
  
  private ArrayList<TimeRange> findAppropriateTimes(ArrayList<TimeRange> givenTimes, long durationOfMeeting) {
      int beginning = TimeRange.START_OF_DAY;
      ArrayList<TimeRange> appropriateTimes = new ArrayList();

      // Loop over times and if the difference between the times is large enough, add it to the list.
      for (TimeRange currentTime : givenTimes) {
          if ((currentTime.start() - beginning) >= durationOfMeeting) {
              appropriateTimes.add(TimeRange.fromStartEnd(beginning, currentTime.start(), false));
            }

          // If the end of the event is further in the day than the beginning, move the beginning to the end of that event
          if (currentTime.end() > beginning) {
              beginning = currentTime.end();
          }
      }
      // If it is the last meeting of the day, add it to the list of times. 
      if ((TimeRange.END_OF_DAY - beginning) > durationOfMeeting) {
          appropriateTimes.add(TimeRange.fromStartEnd(beginning, TimeRange.END_OF_DAY, true));
      }
      return appropriateTimes;
  }
}
