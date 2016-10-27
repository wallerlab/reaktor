<%@ page import="reaktor.Reaction" %>
<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="core-admin">
	<link rel="stylesheet" href="${request.contextPath}/css/ChemDoodleWeb.css" type="text/css">
</head>
<body>
<div class="col-md-10">
	<div class="box">
		<div class="box-header">
			<span class="title">This is your reaction.
			Click on the molecules to see them in our 3-D viewer.</span>
		</div>
		<div class="box-content padded scrollable text-center" style="height: 552px; overflow-y: auto">
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>

			<script type="text/javascript" src="${request.contextPath}/js/ChemDoodleWeb.js"></script>
			<script>
				var transformLine = new ChemDoodle.TransformCanvas3D('transformLine', 500, 350);
				transformLine.specs.set3DRepresentation('Ball and Stick');
				transformLine.specs.atoms_useJMOLColors = true;
				transformLine.specs.backgroundColor = 'black';

				function loadmol(reactionID){
					jQuery.ajax({
						url: "${createLink(controller: 'molecule', action: 'showInReactionViewer')}",
						async:false,
						data : "id="+reactionID,
						dataType: "text",
						success : function(data) {
							var mol=ChemDoodle.readXYZ(data);
							transformLine.loadMolecule(mol);
						},
						error: function(){
							document.getElementById("test").innerHTML = "error";
						}
					});
				}
			</script>
			<div class="row" style="line-height:200%">
				<g:if test="${reactionInstance?.user}">
					<sec:ifAllGranted roles="ROLE_ADMIN">
						<div class="col-md-3 col-md-push-3 col-size-med">
							<strong class="text-info" style="font-size:14px">User:</strong>
						</div>
						<div class="col-md-3 col-md-push-3 col-size-med">
							<span class="text-info" style="font-size:14px">
								<g:link controller="user" action="show" id="${reactionInstance?.user?.id}">${reactionInstance?.user?.encodeAsHTML()}</g:link>
							</span>
						</div>
					</sec:ifAllGranted>
				</g:if>
			</div>
			<div class="row" style="line-height:200%">
				<g:if test="${reactionInstance?.status}">
					<div class="col-md-3 col-md-push-3 col-size-med">
						<strong class="text-info" style="font-size:14px">Status:</strong>
					</div>
					<div class="col-md-3 col-md-push-3 col-size-med">
						<span class="text-info" style="font-size:14px"><g:fieldValue bean="${reactionInstance}" field="status"/></span>
					</div>
				</g:if>
			</div>
			<div class="row" style="line-height:200%">
				<g:if test="${reactionInstance?.reactants}">
					<div class="col-md-3 col-md-push-3 col-size-med">
						<strong class="text-info" style="font-size:14px">Reactants:</strong>
					</div>
					<div class="col-md-3 col-md-offset-3 col-size-med">
						<g:each in="${reactionInstance.reactants}" var="r">
							<div class="row">
								<span class="text-info" style="font-size:14px">
									<a href="#" onclick="loadmol(${r.id})">${r.name}</a>
								</span>
							</div>
						</g:each>
					</div>
				</g:if>
			</div>
			<div class="row" style="line-height:200%">
				<g:if test="${reactionInstance?.products || reactionInstance?.status == 'finished'}">
					<div class="col-md-3 col-md-push-3 col-size-med">
						<strong class="text-info vpadded" style="font-size:14px">Products:</strong>
					</div>
					<g:if test="${!reactionInstance?.products && reactionInstance?.status.contains('finished') || reactionInstance?.status.contains('error')}">
						<div class="col-md-3 col-md-push-3 col-size-med">
							<span class="text-info" style="font-size:14px">
								No products were found for this reaction.
							</span>
						</div>
					</g:if>
					<g:else>
						<div class="col-md-3 col-md-offset-3 col-size-med">
							<g:each in="${reactionInstance?.products}" var="r">
								<div class="row">
									<span class="text-info" style="font-size:14px">
										<a href="#" onclick="loadmol(${r.id})">${r.name}</a>
									</span>
								</div>
							</g:each>
						</div>
					</g:else>
				</g:if>
			</div>
			<g:if test="${reactionInstance?.status == 'finished' || reactionInstance?.status == 'error while calculating'}">
				<br>
				<br>
				<div class="row" style="line-height:200%">
					<div class="col-md-4 col-md-offset-4 col-size-med text-center">
						<g:link class="btn btn-lg btn-info" action="clean" resource="${reactionInstance}"><g:message code="default.button.clean.label" default="Clean" /></g:link>
					</div>
				</div>
				<br>
				<div class="row">Click on "Clean" to clean up your work folder. You will not lose any data.</div>
			</g:if>
		</div>
	</div>
</div>
</body>
</html>
