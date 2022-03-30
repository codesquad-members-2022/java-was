# java-was
Java Web Server Project for CodeSquad Members 2022  

Team : [`테리`](https://github.com/mybloom), [`쿠킴`](https://github.com/ku-kim)  

# Step 1 : HTTP GET 응답

[Step1 PR](https://github.com/codesquad-members-2022/java-was/pull/4)

---

# Step 2 : GET으로 회원가입 기능 구현
[Step2 PR](https://github.com/codesquad-members-2022/java-was/pull/20)


<details>
<summary> 🖼📝 Step 2 결과 </summary>
<div markdown="1">

<details>
<summary> view </summary>
<div markdown="1">

- localhost:8080/index.html
  ![index](https://i.imgur.com/dw8zYPI.jpg)

- localhost:8080/user/form.html
  ![form](https://i.imgur.com/gDlhS8e.jpg)

- 한글 이름 회원가입 (회원가입시 서버에 로그 확인 가능)
  ![회원가입](https://i.imgur.com/78x6ymA.jpg)

</div>
</details>

<details>
<summary> 테스트 </summary>
<div markdown="1">

![request test](https://i.imgur.com/Wb37br5.jpg)

</div>
</details>

</div>
</details>

---

# Step 3 : POST로 회원 가입
[Step3 PR](https://github.com/codesquad-members-2022/java-was/pull/45)


<details>
<summary> 🖼📝 Step 3 결과 </summary>
<div markdown="1">


<details>
<summary> 테스트 </summary>
<div markdown="1">

![test](https://i.imgur.com/IL8QjvH.jpg)

</div>
</details>

</div>
</details>

---

# Step 4 : 쿠키를 이용한 로그인 구현

[Step4 PR](https://github.com/codesquad-members-2022/java-was/pull/61)


<details>
<summary> 🖼📝 Step 4 결과 </summary>
<div markdown="1">

<details>
<summary> View  </summary>
<div markdown="1">

- GET /user/login.html - 로그인 페이지
![login](https://i.imgur.com/0p9h5yy.jpg)

- 로그인 실패 : /user/login_failed.html로 302 리다이렉트
![loginfailed](https://i.imgur.com/JYOeGTh.jpg)

- 로그인 성공 : 서버로부터 쿠키 응답과 /index.html로 302 리다이렉트
![login200](https://i.imgur.com/XgfRDzz.jpg)

- 로그아웃 : 서버로부터 쿠키 기한 끝 응답과 /index.html로 302 리다이렉트
![logout](https://i.imgur.com/ireFZr4.jpg)

</div>
</details>

</div>
</details>
