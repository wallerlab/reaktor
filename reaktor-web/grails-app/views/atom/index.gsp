
<%@ page import="reaktor.Atom" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'atom.label', default: 'Atom')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-atom" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-atom" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
			<thead>
					<tr>
					
						<g:sortableColumn property="element" title="${message(code: 'atom.element.label', default: 'Element')}" />
					
						<g:sortableColumn property="idInMolecule" title="${message(code: 'atom.idInMolecule.label', default: 'Id In Molecule')}" />
					
						<th><g:message code="atom.molecule.label" default="Molecule" /></th>
					
						<g:sortableColumn property="xCoord" title="${message(code: 'atom.xCoord.label', default: 'X Coord')}" />
					
						<g:sortableColumn property="yCoord" title="${message(code: 'atom.yCoord.label', default: 'Y Coord')}" />
					
						<g:sortableColumn property="zCoord" title="${message(code: 'atom.zCoord.label', default: 'Z Coord')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${atomInstanceList}" status="i" var="atomInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${atomInstance.id}">${fieldValue(bean: atomInstance, field: "element")}</g:link></td>
					
						<td>${fieldValue(bean: atomInstance, field: "idInMolecule")}</td>
					
						<td>${fieldValue(bean: atomInstance, field: "molecule")}</td>
					
						<td>${fieldValue(bean: atomInstance, field: "xCoord")}</td>
					
						<td>${fieldValue(bean: atomInstance, field: "yCoord")}</td>
					
						<td>${fieldValue(bean: atomInstance, field: "zCoord")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${atomInstanceCount ?: 0}" />
			</div>
		</div>
	</body>
</html>
