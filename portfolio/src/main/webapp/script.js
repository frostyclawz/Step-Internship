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

google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(drawChart);

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
    var numberOfComments = document.getElementById('comment-number').value;
    const data_response = await fetch('/data?comment-number=' + comment_number);


    // Data_response returns an array to be parsed.
    const data_text = await data_response.json();
 
    // Loops over the returned array to display the messages on the webpage
    var html = '';
    for(var i = 0; i < numberOfComments; i++) {
      html += '<li>' + data_text[i] + '</li>';
      html += '\n';
    }

    // Adds the text to the webpage
    document.getElementById('json-holder').innerHTML = html;
}

async function deleteComments() {
    const data_post = await fetch('/delete-data', {method:'POST'});

    // Removes comments from the webpage.
    document.getElementById('json-holder').innerHTML = '';
}

function drawChart() {
    fetch('/prime-num-data').then(response => response.json())
    .then((primeNumbers) => {
        const data = new google.visualization.DataTable();
        data.addColumn('string', 'Value Number');
        data.addColumn('number', 'Prime Number');
        Object.keys(primeNumbers).forEach((valueNumber) => {
            data.addRow([valueNumber, primeNumbers[valueNumber]]);
        });

        const options = {
            'title' : 'Prime Numbers',
            'hAxis' : {
                'title' : 'Value Number'
            },
            'vAxis' : {
                'title' : 'Prime Number'
            },
            'curveType' : 'Function',
            'width' : 900,
            'height' : 500
        };

        const chart = new google.visualization.LineChart(
            document.getElementById('chart-holder'));
        chart.draw(data, options);
    });
}