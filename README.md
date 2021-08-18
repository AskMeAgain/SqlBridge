# Getting Started

This sql bridge allows you to simply execute sql via rest. This bridge is also compatible with Postman and allows for an easy display of the data.
Whenever you do a request, switch to the "Visualize" tab in the response to see the data in a table.


## Installation

This project is only a default Spring app. The important part can be found inside "SqlController.java". 
Just copy this file into your project and provide a jdbi instance as bean and you are good to go.

## Usage

A GET restcall to /query with the sql query as a raw string body. Checkout the complete postman collection in this project (TestCollection.postman_collection.json)

## Dependencies

This bridge uses Jdbi, but could be easily adapted to jdbc only.

## Postman

Create a folder in Postman and put the following code into the "Test" section. All SQL queries should be in a child of the folder.

```javascript
var template = `
<table bgcolor="#FFFFFF">
<tr>
{{#each header}}
<th>{{this}}</th>
{{/each}}
</tr>
    {{#each data}}
        <tr>
        {{#each this}}
            <td>{{this}}</td>
        {{/each}}
        </tr>
    {{/each}}
</table>
`;

var header = pm.response.json()[0];
var data = pm.response.json().slice(1);

pm.visualizer.set(template, {
header: header,
data: data
});
```