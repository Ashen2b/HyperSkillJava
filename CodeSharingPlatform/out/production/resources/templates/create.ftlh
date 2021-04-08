<!DOCTYPE html>
<html lang="en">
<head>
    <title>Create</title>
    <link rel="stylesheet" href="/style.css">
</head>
<body>
    <label for="code_snippet" class="plain_text">Enter your code here:</label>
    <br>
    <textarea id="code_snippet"></textarea>
    <br>
    <label for="time_restriction" class="plain_text">Time restriction:</label><input id="time_restriction" type="text"/>
    <br>
    <label for="views_restriction" class="plain_text">Views restriction:</label><input id="views_restriction" type="text"/>
    <br>
    <button id="send_snippet" type="submit" onclick="send()">Submit</button>
    <script src="/main.js"></script>
</body>
</html>