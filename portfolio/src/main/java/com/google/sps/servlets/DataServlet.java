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

package com.google.sps.servlets;

import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private static final String CONTENT_TYPE = "application/json;";
  private static final String TEXT_INPUT = "text-input";
  private static final String REDIRECT = "/home.html";

  private String convertToJson(ArrayList<String> comments) {
    Gson gson = new Gson();
    String json = gson.toJson(comments);
    return json;
  }


  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String text = getParameter(request, TEXT_INPUT, "");
    
    Entity commentEntity = new Entity("comment");
    commentEntity.setProperty("comment", text);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);

    response.sendRedirect(REDIRECT);
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("comment");
    int numComments = Integer.parseInt(request.getParameter("comment-number"));

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery comments = datastore.prepare(query);

    ArrayList<String> comments_list = new ArrayList();
    int counter = 0;
    for (Entity commentEntity : comments.asIterable()) {
      String comment = (String) commentEntity.getProperty("comment");

      comments_list.add(comment);
      counter += 1;

      if (counter == numComments) {
          break;
      }
    }

    Gson gson = new Gson();

    response.setContentType(CONTENT_TYPE);
    response.getWriter().println(convertToJson(comments_list));
  }  

}