<!DOCTYPE html>
<html lang="en">
<head>
    <title>Code</title>
    <link rel="stylesheet" href="/style.css">
    <link rel="stylesheet" href="/default.css">
</head>
<body>
    <script src="/highlight.pack.js"></script>
    <script>hljs.initHighlightingOnLoad()</script>

    <span id="load_date">${snippet.date}</span>
    <#if snippet.notOriginalViews>
        <#if snippet.getViewsLeft() gt -1>
            <span id="views_restriction" class="plain_text"><b>${snippet.getViewsLeft()}</b> more views allowed</span><br>
        </#if>
    </#if>
    <#if snippet.getTimeLeft() gt 0>
        <span id="time_restriction" class="plain_text">The code will be available for <b>${snippet.getTimeLeft()}</b> seconds</span><br>
    </#if>
    <pre class="java" id="code_snippet"><code>${snippet.getCode()}</code></pre>
    <script>hljs.highlightAll()</script>
</body>
</html>