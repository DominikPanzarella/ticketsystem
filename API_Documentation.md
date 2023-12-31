# Ticket Management API 

## Introduction
The Ticket management allows users to create, read, update and delete tickets (i.e CRUD operations via HTTP protocol).

## Base URL
The base URL for all API requests is:
http://localhost:8080/tickets

## Endpoints

### Create a Tickets

The values of a new Ticket could be sent in the body of the request in two ways:

- in the form of JSON (`Content-Type: application/json`)

  - **URL**: `/tickets`
  - **Method**: `POST`
  - **Description**: Create a new ticket.
  - **Request Body**:
  ```json
    {
    "title": "New incident",
    "description": "The application is not working.",
    "author": "marco"
    }
  ```

- in the same syntax as the query string, but in the body (`Content-Type: application/x-www-form-urlencoded`)
    - **URL**: `/tickets?title=proof&description=text&author=marco`
    - **Method**: `POST`
    - **Description**: Create a new ticket.

In both cases the **Response** is a JSON of the Ticket inserted:
```json
{
 "title": "New incident",
 "description": "The application is not working.",
 "author": "marco"
}
```
with **Status Conde** : `200 SC_OK`

### Get a Ticket

- **URL**: `/tickets/{id}`
- **Method**: `GET`
- **Description**: Get a ticket by ID.
- **Responses**:
  - Ticket Found:
    - **Status Code**: `302 SC_FOUND`
    - **Response**:
        ```json
        {
        "id": 1,
        "title": "New Ticket",
        "description": "This is a new ticket."
        }
        ```
    - Ticket Not Found:
      - **Status Code**: `404 SC_NOT_FOUND`
      - **Response**:
        ```
            Ticket Not Found
        ```
    - Invalid URL:
      - **Status Code**: `400 C_BAD_REQUEST`
      - **Response**:
        ```
            Invalid URL
        ```
        
### Get All the Tickets
- **URL**: `/tickets`
- **Method**: `GET`
- **Description**: Get all tickets.
- **Responses**:
  - Correct URL:
    - **Status Code**: `200 SC_OK`
      - **Response**:
        ```json
        [
            {
                "title": "til",
                "description": "desc",
                "author": "aut",
                "id": 1
            },
            {
                "title": "til",
                "description": "desc",
                "author": "aut",
                "id": 2
            },
            {
                "title": "proof",
                "description": "text",
                "author": "marco",
                "id": 3
            }
         ]
        ```
  - Invalid URL:
    - **Status Code**: `404 SC_NOT_FOUND`
    - **Response**:
    ```html
        <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html;charset=ISO-8859-1" />
            <title>Error 404 Not Found</title>
        </head>
        <body>
            <h2>HTTP ERROR 404 Not Found</h2>
            <table>
                <tr>
                    <th>URI:</th>
                    <td>/ticket</td>
                </tr>
                <tr>
                    <th>STATUS:</th>
                    <td>404</td>
                </tr>
                <tr>
                    <th>MESSAGE:</th>
                    <td>Not Found</td>
                </tr>
                <tr>
                    <th>SERVLET:</th>
                    <td>default</td>
                </tr>
            </table>
            <hr /><a href="https://eclipse.org/jetty">Powered by Jetty:// 11.0.12</a>
            <hr />
        </body>
        </html>
      ```
      
### Delete a Ticket
- **URL**: `/tickets/{id}`
- **Method**: `DELETE`
- **Description**: Delete a Ticket by id.
- **Responses**:

  - Correct URL:
    - Ticket found:
      - **Status Code**: `204 SC_NO_CONTENT`
      - **Response**:
        Empty Page
      
    - Ticket not found:
      - **Status Code**: `404 SC_NOT_FOUND`
      - **Response**:
        ```
        Invalid Id
        ```
  - Invalid URL:
    - **Status Code**: `400 SC_BAD_REQUEST`
    - **Response**:
      ```
      Invalid URL
      ```

### Complete Update/Modification of a Ticket
- **URL**: `/tickets/{id}`
- **Method**: `PUT`
- **Description**: Update a Ticket by id. The request body must contain all the ticket's attribute.
- **Request Body**:
  The request body should contain a JSON object representing the ticket's new data.
Example Request Body:
```json
{
  "title": "Updated Ticket Title",
  "description": "This is the updated ticket description.",
  "author": "marco"
}
```
- **Responses**:
  - Invalid URL:
    - **Status Code**: `400 SC_BAD_REQUEST`
    - **Response**:
    ```html
        Invalid URL
    ```
    - Correct URL:
      - Invalid ID:
        - **Status Code**: `404 SC_NOT_FOUND`
        - **Response**:
          ```
          Invalid Id
          ```
      - Correct ID:
        - All data inserted:
          - **Status Code**: `400 SC_BAD_REQUEST`
          - **Response**:
            ```
              Missing Data
            ```
        - Missing data:
          - **Status Code**:  `200 SC_OK`
          - **Response**:
          ```json
          {
              "title": "Updated Ticket Title",
              "description": "This is the updated ticket description.",
              "author": "marco"
          }
          ```
          
### Partial update/modification of a ticket
- **URL**: `/tickets/{id}`
- **Method**: `PATCH`
- **Description**: Update a Ticket by id. If one attribute is not inserted in the request body, the old value will be used.
- **Request Body**:
  The request body should contain a JSON object representing the ticket's new data.
  Example Request Body:
    ```json
    {
      "description": "This is the partial updated ticket description."
    }
    ```
- **Responses**:
    - Invalid URL:
        - **Status Code**: `400 SC_BAD_REQUEST`
        - **Response**:
      ```html
          Invalid URL
      ```
        - Correct URL:
            - Invalid ID:
                - **Status Code**: `404 SC_NOT_FOUND`
                - **Response**:
                  ```
                  Invalid Id
                  ```
            - Correct ID:
                - **Status Code**: `200 SC_OK`
                - **Response**:
                  ```json
                  {
                  "title": "Updated Ticket Title",
                  "description": "This is the partial updated ticket description.",
                  "author": "marco"
                  }
                  ```
