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

/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      ['Hello world!', '¡Hola Mundo!', '你好，世界！', 'Bonjour le monde!'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
};

/**
 * Function that adds text from DataServlet.java to the page
 */
async function getDataServletText() {
    // Fetches data from DataServlet.java
    const response = await fetch('/data');

    // Returns text from the data fetched from the servlet.
    const text = await response.text();

    // Adds text to page
    document.getElementById('text-holder').innerText = text;
}

/**
 * Function that takes a json from DataServlet.java and parses the text to 
 * print on the page
 */
async function getDataJson() {
    const data_response = await fetch('/data');

    // It is an array here...
    const data_text = await data_response.json();
 
    // Loops over the returned array to display the messages on the webpage
    var html = '';
    for(var i = 0; i < data_text.length; i++) {
      html += '<li>' + data_text[i] + '</li>';
      html += '\n';
    }

    // Adds the text to the webpage
    document.getElementById('json-holder').innerHTML = html;

}
