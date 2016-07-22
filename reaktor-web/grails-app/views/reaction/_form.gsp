<%@ page import="reaktor.Reaction" %>



<div class="fieldcontain ${hasErrors(bean: reactionInstance, field: 'user', 'error')} ">
	<label for="user">
		<g:message code="reaction.user.label" default="User" />
		
	</label>
	<g:select id="user" name="user.id" from="${reaktor.User.list()}" optionKey="id" value="${reactionInstance?.user?.id}" class="many-to-one" noSelection="['null': '']"/>

</div>

<div class="fieldcontain ${hasErrors(bean: reactionInstance, field: 'status', 'error')} required">
	<label for="status">
		<g:message code="reaction.status.label" default="Status" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="status" from="${reactionInstance.constraints.status.inList}" required="" value="${reactionInstance?.status}" valueMessagePrefix="reaction.status"/>

</div>

<div class="fieldcontain ${hasErrors(bean: reactionInstance, field: 'products', 'error')} ">
	<label for="products">
		<g:message code="reaction.products.label" default="Products" />
		
	</label>
	<g:select name="products" from="${reaktor.Molecule.list()}" multiple="multiple" optionKey="id" size="5" value="${reactionInstance?.products*.id}" class="many-to-many"/>

</div>

<div class="fieldcontain ${hasErrors(bean: reactionInstance, field: 'reactants', 'error')} ">
	<label for="reactants">
		<g:message code="reaction.reactants.label" default="Reactants" />
		
	</label>
	<g:select name="reactants" from="${reaktor.Molecule.list()}" multiple="multiple" optionKey="id" size="5" value="${reactionInstance?.reactants*.id}" class="many-to-many"/>

</div>

