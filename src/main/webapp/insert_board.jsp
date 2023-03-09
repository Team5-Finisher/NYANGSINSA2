<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="nss" tagdir="/WEB-INF/tags/"%>

<!DOCTYPE html>
<html lang="ko">

<head>
<meta charset="UTF-8">
<meta name="description" content="Ogani Template">
<meta name="keywords" content="Ogani, unica, creative, html">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<title>냥신사 | CONTACT</title>

<!-- google Font -->
<link href="https://fonts.googleapis.com/css2?family=Cairo:wght@200;300;400;600;900&display=swap" rel="stylesheet">

<!-- Css Styles -->
<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
<link rel="stylesheet" href="css/font-awesome.min.css" type="text/css">
<link rel="stylesheet" href="css/elegant-icons.css" type="text/css">
<link rel="stylesheet" href="css/nice-select.css" type="text/css">
<link rel="stylesheet" href="css/jquery-ui.min.css" type="text/css">
<link rel="stylesheet" href="css/owl.carousel.min.css" type="text/css">
<link rel="stylesheet" href="css/slicknav.min.css" type="text/css">
<link rel="stylesheet" href="css/style.css" type="text/css">
</head>
<style>
/* CKEditor Style */
.ck.ck-editor {
	max-width: 100%;
}

.ck-editor__editable {
	min-height: 500px;
}

</style>

<body>
	<nss:header />
	
	<!-- 메인 화면에는 제외 -->
	<!-- Breadcrumb Section Begin -->
	<section class="breadcrumb-section set-bg" data-setbg="">
		<div class="container">
			<div class="row">
				<div class="col-lg-12 text-center">
					<div class="breadcrumb__text">
						<h2>contact</h2>
						<!-- <div class="breadcrumb__option">
                     <a href="./index.html">홈</a>
                  </div> -->
					</div>
				</div>
			</div>
		</div>
	</section>
	<!-- Breadcrumb Section End -->
	
	<!-- 자랑하기 Begin -->
	<div class="contact-form spad">
		<div class="container">
			<div class="row">
				<div class="col-lg-12">
					<div class="contact__form__title">
						<h2>고양이 자랑하기</h2>
						<hr>
					</div>
				</div>
			</div>
			<form action="insertBoard.do" method="POST">
				<div class="row">
					<div class="row1" style="width: 10%; margin-left: 15px">
						<div style="padding-bottom: 45px;">
							<p style="font-weight: bold;">제목</p>
						</div>
						<div style="padding-bottom: 495px;">
							<p style="font-weight: bold;">내용</p>
						</div>
					</div>
					<div class="row2" style="width: 90%; margin-left: -15px">
						<div class="col-lg-12 col-md-12">
							<input type="text" name="title" required>
						</div>
						<div class="col-lg-12 text-center">
							<textarea name="content" id="editor"></textarea>
							<button type="submit" class="site-btn" style="margin-top: 30px;">작성하기</button>
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>
	<!-- 자랑하기 End -->

	<!-- Contact Section Begin -->
	<section class="contact spad">
		<div class="container">
			<div class="row">
				<div class="col-lg-3 col-md-3 col-sm-6 text-center">
					<div class="contact__widget">
						<span class="icon_phone"></span>
						<h4>대표 번호</h4>
						<p>+82 02-0202-0202</p>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-6 text-center">
					<div class="contact__widget">
						<span class="icon_pin_alt"></span>
						<h4>주소</h4>
						<p>서울특별시 강남구 역삼동 골목길</p>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-6 text-center">
					<div class="contact__widget">
						<span class="icon_clock_alt"></span>
						<h4>운영 시간</h4>
						<p>10:00 ~ 18:00</p>
					</div>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-6 text-center">
					<div class="contact__widget">
						<span class="icon_mail_alt"></span>
						<h4>Email</h4>
						<p>nyangsinsa@gmail.com</p>
					</div>
				</div>
			</div>
		</div>
	</section>
	<!-- Contact Section End -->

	<!-- Map Begin -->
	<div class="map">
		<iframe src="https://maps.google.com/maps?q=%EC%84%9C%EC%9A%B8%ED%8A%B9%EB%B3%84%EC%8B%9C%20%EA%B0%95%EB%82%A8%EA%B5%AC%20%EC%97%AD%EC%82%BC%EB%8F%99%20736-7&t=&z=15&ie=UTF8&iwloc=&output=embed" height="500" style="border: 0;" allowfullscreen="" aria-hidden="false" tabindex="0"></iframe>
		<div class="map-inside">
			<i class="icon_pin"></i>
			<div class="inside-widget">
				<h4>코리아 it</h4>
				<ul>
					<li>번호 : +82 02-0202-0202</li>
					<li>주소 : 서울특별시 강남구 역삼동 골목길</li>
				</ul>
			</div>
		</div>
	</div>
	<!-- Map End -->

	<!-- Contact Form Begin -->
	<div class="contact-form spad">
		<div class="container">
			<div class="row">
				<div class="col-lg-12">
					<div class="contact__form__title">
						<h2>의견 보내기</h2>
					</div>
				</div>
			</div>
			<form id="contact-form">
				<div class="row">
					<div class="col-lg-6 col-md-6">
						<input type="text" id="name" name="name" placeholder="이름 입력" required>
						<input type="hidden" name="contact_number">
					</div>
					<div class="col-lg-6 col-md-6">
						<input type="email" id="email2" name="email2" placeholder="이메일 입력" required>
					</div>
					<div class="col-lg-12 text-center">
						<textarea name="message" id="message" placeholder="의견 작성" required></textarea>
						<button type="submit" class="site-btn">보내기</button>
					</div>
				</div>
			</form>
		</div>
	</div>

	<!-- Contact Form End -->

	<!-- Footer Section Begin -->
	<nss:footer />
	<!-- Footer Section End -->


	<!-- Js Plugins -->
	<script src="js/jquery-3.3.1.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/jquery.nice-select.min.js"></script>
	<script src="js/jquery-ui.min.js"></script>
	<script src="js/jquery.slicknav.js"></script>
	<script src="js/mixitup.min.js"></script>
	<script src="js/owl.carousel.min.js"></script>
	<script src="js/main.js"></script>
	<script src="js/email.js"></script>
	<script src="https://cdn.ckeditor.com/ckeditor5/36.0.1/classic/ckeditor.js"></script>
	<!-- ckeditor 스크립트 -->
	<script>      
	   ClassicEditor
	   .create(document.querySelector('#editor'), {
	      extraPlugins: [MyCustomUploadAdapterPlugin], // imageUpload Adapter [UploadAdapter.js]
	   })
	   .then(editor => {
	      console.log('Editor was initialized');
	   })
	   .catch(error => {
	      console.error(error);
	   });
	   
	   // imageUpload Adapter 호출 및 생성
	    function MyCustomUploadAdapterPlugin(editor) { // 플러그인 함수
	        editor.plugins.get('FileRepository').createUploadAdapter = (loader) => {
	            return new ckEditorImageUpload(loader); // imageUpload Adapter 함수 [UploadAdapter.js]
	        }
	    }
	   
	   class ckEditorImageUpload {
	      constructor(loader) {
	         this.loader = loader;
	      }

	      upload() {
	         return this.loader.file.then( file => new Promise(((resolve, reject) => {
	            this._initRequest();
	           // this._initListeners( resolve, reject, file );
	            this._sendRequest( file );
	         })))
	      }

	      _initRequest() {
	         const xhr = this.xhr = new XMLHttpRequest();
	         xhr.open('POST', 'writeBoard.do', true); 
	         xhr.responseType = 'json';
	      }

	      _initListeners(resolve, reject, file) {
	         const xhr = this.xhr;
	         const loader = this.loader;
	         const genericErrorText = '파일을 업로드 할 수 없습니다.'
			
	         xhr.addEventListener('error', () => {reject(genericErrorText)})
	         xhr.addEventListener('abort', () => reject())
	         xhr.addEventListener('load', () => {
	            const response = xhr.response
	            if(!response || response.error) {
	               return reject( response && response.error ? response.error.message : genericErrorText );
	            }
	            setTimeout(() => {
	               resolve({
	                  default: response.url //업로드된 파일 주소
	               })
	            }, 5000);
	         })
	      }

	      _sendRequest(file) {
	         const data = new FormData()
	         data.append('upload',file)
	         this.xhr.send(data)
	      }
	   }
	   </script>

	<!-- TOP 버튼 -->
	<div style="width: 120px; position: fixed; bottom: 80px; right: 100px; z-index: 1;">
		<a href="#">
			<button type="button" class="button-top">▲ 맨위로</button>
		</a>
	</div>
</body>


</html>