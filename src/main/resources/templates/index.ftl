<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Home</title>
    <script src="/static/js/angular.min.js"></script>
    <script src="/static/js/app.js"></script>
</head>
<body ng-app="myApp">
<div ng-controller="LoginController">
    <h1>Welcome to the Website</h1>
    <form ng-submit="login()">
        <label for="username">Username:</label>
        <input type="text" id="username" ng-model="credentials.username">
        <br>
        <label for="password">Password:</label>
        <input type="password" id="password" ng-model="credentials.password">
        <br>
        <button type="submit">Login</button>
    </form>
    <p>{{message}}</p>
</div>
</body>
</html>
