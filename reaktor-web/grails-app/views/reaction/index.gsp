<%@ page import="reaktor.Reaction" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="core-admin">
		<g:set var="entityName" value="${message(code: 'reaction.label', default: 'Reaction')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<div class="col-md-12">
		<div class="box">
		<div id="list-reaction" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table class="table">
			<thead>
					<tr>
					
						<th><g:message code="reaction.user.label" default="ID" /></th>
					
						<g:sortableColumn property="molecules" title="${message(code: 'reaction.status.label', default: 'Reactants')}" />
					
						<g:sortableColumn property="status" title="${message(code: 'reaction.status.label', default: 'Status')}" />
					
						<g:sortableColumn property="lastUpdated" title="${message(code: 'reaction.status.label', default: 'Last Updated')}" />
					
						<g:sortableColumn property="dateCreated" title="${message(code: 'reaction.status.label', default: 'Date Created')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${reactionInstanceList}" status="i" var="reactionInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${reactionInstance.id}">${fieldValue(bean: reactionInstance, field: "id")}</g:link></td>
					
						<td>${fieldValue(bean: reactionInstance, field: "molecules")}</td>
					
						<td>${fieldValue(bean: reactionInstance, field: "status")}</td>
					
						<td>${fieldValue(bean: reactionInstance, field: "lastUpdated")}</td>
					
						<td>${fieldValue(bean: reactionInstance, field: "dateCreated")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${reactionInstanceCount ?: 0}" />
			</div>
		</div>
		</div>
		</div>
	</body>
</html>
