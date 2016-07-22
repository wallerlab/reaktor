
<%@ page import="reaktor.Atom" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'atom.label', default: 'Atom')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-atom" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<sec:ifAllGranted roles="ROLE_ADMIN">
					<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
					<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
				</sec:ifAllGranted>
			</ul>
		</div>
		<div id="show-atom" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list atom">
			
				<g:if test="${atomInstance?.element}">
				<li class="fieldcontain">
					<span id="element-label" class="property-label"><g:message code="atom.element.label" default="Element" /></span>
					
						<span class="property-value" aria-labelledby="element-label"><g:fieldValue bean="${atomInstance}" field="element"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${atomInstance?.molecule}">
				<li class="fieldcontain">
					<span id="molecule-label" class="property-label"><g:message code="atom.molecule.label" default="Molecule" /></span>
					
						<span class="property-value" aria-labelledby="molecule-label"><g:link controller="molecule" action="show" id="${atomInstance?.molecule?.id}">${atomInstance?.molecule?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${atomInstance?.xCoord}">
				<li class="fieldcontain">
					<span id="xCoord-label" class="property-label"><g:message code="atom.xCoord.label" default="X Coord" /></span>
					
						<span class="property-value" aria-labelledby="xCoord-label"><g:fieldValue bean="${atomInstance}" field="xCoord"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${atomInstance?.yCoord}">
				<li class="fieldcontain">
					<span id="yCoord-label" class="property-label"><g:message code="atom.yCoord.label" default="Y Coord" /></span>
					
						<span class="property-value" aria-labelledby="yCoord-label"><g:fieldValue bean="${atomInstance}" field="yCoord"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${atomInstance?.zCoord}">
				<li class="fieldcontain">
					<span id="zCoord-label" class="property-label"><g:message code="atom.zCoord.label" default="Z Coord" /></span>
					
						<span class="property-value" aria-labelledby="zCoord-label"><g:fieldValue bean="${atomInstance}" field="zCoord"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<sec:ifAllGranted roles="ROLE_ADMIN">
				<g:form url="[resource:atomInstance, action:'delete']" method="DELETE">
					<fieldset class="buttons">
						<g:link class="edit" action="edit" resource="${atomInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
						<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
					</fieldset>
				</g:form>
			</sec:ifAllGranted>
		</div>
	</body>
</html>
