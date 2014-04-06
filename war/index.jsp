<!DOCTYPE html>

<html>
<head>
  <%@include file="includes/head.jsp" %>
</head>

<body>

  <input id="home-address" type="text" placeholder="Please enter your home address." />

  <button id="i-am-hungry" onclick="feelingHungry()">I'm hungry!</button>
  <button id="i-am-thirsty" onclick="feelingThirsty()">I'm thirsty</button>

  <%@include file="includes/footer.jsp" %>

</body>
</html>
