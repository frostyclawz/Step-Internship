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

import java.util.ArrayList;
import java.util.Arrays;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Scanner;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/prime-num-data")
public class PrimeNumberServlet extends HttpServlet {

    private static final String CONTENT_TYPE = "application/json";

    private LinkedHashMap<Integer, Integer> primeNumbers = new LinkedHashMap<>();

    @Override
    public void init() {
        Scanner scanner = new Scanner(getServletContext().getResourceAsStream(
            "/WEB-INF/prime.csv"));
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] cells = line.split(",");

            Integer valueNumber = Integer.valueOf(cells[0]);
            Integer primeNumber = Integer.valueOf(cells[1]);

            primeNumbers.put(valueNumber, primeNumber);
        }
        scanner.close();
    }
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(CONTENT_TYPE);
        Gson gson = new Gson();
        String json = gson.toJson(primeNumbers);
        response.getWriter().println(json);
    }
}