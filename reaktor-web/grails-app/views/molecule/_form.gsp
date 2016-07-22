<%@ page import="reaktor.Molecule" %>



<div class="fieldcontain ${hasErrors(bean: moleculeInstance, field: 'atoms', 'error')} ">
	<label for="atoms">
		<g:message code="molecule.atoms.label" default="Atoms" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${moleculeInstance?.atoms?}" var="a">
    <li><g:link controller="atom" action="show" id="${a.id}">${a?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="atom" action="create" params="['molecule.id': moleculeInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'atom.label', default: 'Atom')])}</g:link>
</li>
</ul>


</div>

<div class="fieldcontain ${hasErrors(bean: moleculeInstance, field: 'elementMap', 'error')} ">
	<label for="elementMap">
		<g:message code="molecule.elementMap.label" default="Element Map" />
		
	</label>
	

</div>

<div class="fieldcontain ${hasErrors(bean: moleculeInstance, field: 'name', 'error')} required">
	<label for="name">
		<g:message code="molecule.name.label" default="Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="name" required="" value="${moleculeInstance?.name}"/>

</div>

