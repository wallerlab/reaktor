<!DOCTYPE html>
<html lang="en">
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

<g:if test="${flash.message}">
	<div class="col-md-12">
		<div class="box text-center">
			<div class="box-content padded">
				<div class="message" role="status" style="color:red"><i class="icon-warning-sign"></i> ${flash.message}</div>
			</div>
		</div>
	</div>
</g:if>

<div class="col-md-7">
	<div class="box text-center">
		<div class="box-header">
    		<div class="title">Draw Molecules</div>
    	</div>	
    	<div class="box-content padded">
		<p class="news-title">Please draw two molecules, press "Make 3D", then submit.</p>
		<script type="text/javascript" src="${request.contextPath}/js/marvin/marvin.js"></script>
		<script type="text/javascript">
			function exportMol() {
				if (document.MSketch != null) {
					if(document.MSketch.getAtomCount() != 0){
						var s = document.MSketch.getMol("cml:H");
						document.MolForm.MolTxt1.value = s;
					}
					else{
						document.MolForm.MolTxt1.value = null;
					}
				} else {
					alert("Cannot import molecule:\n"
							+ "no JavaScript to Java communication in your browser.\n");
				}
			}
			function clean3D() {
				if (document.MSketch != null) {
					document.MSketch.clean3D();
				}
			}

			msketch_name = "MSketch";
			msketch_begin("${request.contextPath}/js/marvin", 520, 445);
			msketch_end();
		</script>
		<p>
			<input class="btn btn-blue" value="Make 3D" onClick="clean3D()">
			<g:form name="MolForm" controller="calculate">
				<g:hiddenField name="MolTxt1" />
				<g:actionSubmit class="btn btn-blue" value="Submit" onClick="exportMol()"
					action="calculate"></g:actionSubmit>
			</g:form>
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
	<div class="col-md-12">
        <g:render template="userindex"/>
  </div>
  </div>
  

</sec:ifLoggedIn>
</html>