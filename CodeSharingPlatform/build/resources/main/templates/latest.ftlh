<!DOCTYPE html>
<html lang="en">
<head>
    <title>Latest</title>
    <link rel="stylesheet" href="/style.css">
    <link rel="stylesheet" href="/default.css">
</head>
<body>
    <script src="/highlight.pack.js"></script>
    <script>hljs.initHighlightingOnLoad()</script>
    <#list snippets as snippets>
        <span id="load_date">${snippets.date}</span>
        <pre class="java" id="code_snippet"><code>${snippets.code}</code></pre>
    </#list>
<script>hljs.highlightAll()</script>
</body>
</html>