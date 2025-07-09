<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Connexion</title>
    <style>
        body { font-family: Arial, sans-serif; }
        .login-container { width: 300px; margin: 100px auto; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; }
        input[type="text"], input[type="password"] { width: 100%; padding: 8px; }
        button { padding: 10px 15px; background-color: #4CAF50; color: white; border: none; cursor: pointer; }
        .error { color: red; }
        .user-type { margin: 10px 0; }
    </style>
</head>
<body>
    <div class="login-container">
        <h2>Connexion</h2>
        
        <% if (request.getAttribute("errorMessage") != null) { %>
            <p class="error">${errorMessage}</p>
        <% } %>

        <form action="login" method="post">
            <div class="form-group">
                <label for="mail">Email:</label>
                <input type="text" id="mail" name="mail" required>
            </div>
            
            <div class="form-group">
                <label for="password">Mot de passe:</label>
                <input type="password" id="password" name="password" required>
            </div>
            
            <div class="user-type">
                <label>
                    <input type="radio" name="userType" value="admin" required> Administrateur
                </label>
                <label>
                    <input type="radio" name="userType" value="client"> Client
                </label>
            </div>
            
            <button type="submit">Se connecter</button>
        </form>
    </div>
</body>
</html>