<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Confirmation de Réservation</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            line-height: 1.6;
            text-align: center;
        }
        .success-message {
            color: #4CAF50;
            font-size: 18px;
            margin: 20px 0;
        }
        .reservation-id {
            font-weight: bold;
            font-size: 20px;
        }
        .back-link {
            display: inline-block;
            margin-top: 20px;
            padding: 10px 15px;
            background-color: #f0f0f0;
            color: #333;
            text-decoration: none;
            border-radius: 4px;
        }
        .back-link:hover {
            background-color: #e0e0e0;
        }
    </style>
</head>
<body>
    <h1>Réservation confirmée</h1>
    
    <div class="success-message">
        Votre réservation a été créée avec succès !
    </div>
    
    <div>
        Numéro de réservation: <span class="reservation-id">${param.id}</span>
    </div>
    
    <a href="reservationForm.jsp" class="back-link">Faire une nouvelle réservation</a>
</body>
</html>