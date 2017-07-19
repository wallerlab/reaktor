<!DOCTYPE html>
<html lang="en">
<header>
	<script src="${request.contextPath}/js/marvinjs/gui/lib/promise-1.0.0.min.js"></script>
	<script src="${request.contextPath}/js/marvinjs/js/marvinjslauncher.js"></script>


</header>
	<meta name="layout" content="core-admin">
	
	
	<sec:ifNotLoggedIn>
	<div class="col-md-12">
		<div class="box text-center">
			<div class="box-header">
				<div class="title">About Reaktor</div>
			</div>
			<div class="box-content padded">
			<p class="news-title">Reaktor is a product-finding application. You
                    can add your reactants here, then come back for the results.</p>
			<div class="row">
			<div class="col-md-4">
				<div class="box text-center">
					<div class="box-header">
						<div class="title">Molecule Sketcher</div>
					</div>
					<img src="${request.contextPath}/images/drawMolecule.png" alt="" class="thumbnail">
					<p class="news-title">You can draw your reactants in our 2-D sketcher</p>
				</div>
			</div>
			<div class="col-md-4">
				<div class="box text-center">
					<div class="box-header">
						<div class="title">File Uploader</div>
					</div>
					<img src="${request.contextPath}/images/upload.png" alt="" class="thumbnail">
					<p class="news-title">You can upload your own .xyz files</p>
				</div>
			</div>
			<div class="col-md-4">
				<div class="box text-center">
					<div class="box-header">
						<div class="title">Reaction Viewer</div>
					</div>
					<img src="${request.contextPath}/images/reaction.png" alt="" class="thumbnail">
					<p class="news-title">You can view your already submitted reactions</p>
				</div>
			</div>
			</div>
			<p class="news-title">In order to enjoy the full functionality of Reaktor, 
				please login or create a user account.
			</p>
			<a href="login" class="btn btn-lg btn-info">Log In</a>
			<a href="register" class="btn btn-lg btn-info">Create Account</a>
			</div>
		</div>
	</div>
	</sec:ifNotLoggedIn>

<sec:ifLoggedIn>

	<div id="messageDiv"></div>

<div class="col-md-7">
	<div class="box text-center">
		<div class="box-header">
    		<div class="title">Draw Molecules</div>
    	</div>	
    	<div class="box-content padded">
		<p class="news-title">Please draw two molecules, then choose either
		Reaction or Aggregation.</p>

			<g:render template="marvinjs"/>

			<p>
			<button class="btn btn-blue" id="reaction">Reaction</button>
			<button class="btn btn-blue" id="aggregation">Aggregation</button>
			</p>


		</div>
	</div>
	
<sec:ifAllGranted roles="ROLE_ADMIN">
<div class="col-md-12">
	<div class="box text-center">
		<div class="box-header">
    		<div class="title">Admin Buttons</div>
    	</div>	
    	<div class="box-content padded">
		<g:link class="btn btn-blue" controller="user" action="index">Users</g:link>
		<g:link class="btn btn-blue" controller="reaction" action="index">Reactions</g:link>
		</div>
	</div>
</div>
</sec:ifAllGranted>

</div>
	
 <!-- File Upload Section -->
 <div class="col-md-5">
	<div class="box">
  		<div class="box-header">
    		<div class="title">Upload (only for .xyz files):</div>
  		</div>
    	<div class="box-content padded">
        	<div id="homeSearch" class="text-center">
				<div class="text-center">
					<uploadr:add name="reaktorUploader" path="IncomingFiles" direction="up" maxVisible="8" 
						 colorPicker="true" maxSize="204800" allowedExtensions="xyz"/>
    			</div>
				<br />
    			<div class="col-md-12">
    				<p class="text-center">When you are done uploading all your files, click here
    					to send your reaction to our server</p>
    			</div>
    			<div class="col-md-12 text-center">
    				<g:link class="btn btn-blue" controller="calculate" action="sendFiles">Calculate Products</g:link>
    			</div>
        	</div>
    	</div>
	</div>
                
                
		<%--Reaction Viewer--%>
	<div class="col-md-12" id="userIndex">
        <g:render template="userindex"/>
  </div>
  </div>
  

</sec:ifLoggedIn>
</html>