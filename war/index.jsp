<!DOCTYPE html>

<html>
<head>
  <%@include file="includes/head.jsp" %>
</head>

<body>
  <section id="feeling">
    <div class="inner">
      <input id="home-address" type="text" placeholder="Please enter your home address." />

      <button id="i-am-hungry" onclick="feelingHungry()">I'm hungry!</button>
      <button id="i-am-thirsty" onclick="feelingThirsty()">I'm thirsty!</button>
    </div>
  </section>

  <section id="categories">
    <div class="inner">
      <h1 id="title"></h1>
      <h2 id="subtitle"></h2>

      <div id="category-choices">
        <div class="choice">
          <div class="inner">
            <h2>Indian Food</h2>
          </div>
        </div>

        <div class="choice">
          <div class="inner">
            <h2>Indian Food</h2>
          </div>
        </div>

        <div class="choice">
          <div class="inner">
            <h2>Indian Food</h2>
          </div>
        </div>
      </div>

    </div>

  </section>

  <%@include file="includes/footer.jsp" %>

</body>
</html>
