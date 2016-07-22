<%@ page import="reaktor.Atom" %>



<div class="fieldcontain ${hasErrors(bean: atomInstance, field: 'element', 'error')} required">
	<label for="element">
		<g:message code="atom.element.label" default="Element" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="element" required="" value="${atomInstance?.element}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: atomInstance, field: 'idInMolecule', 'error')} required">
	<label for="idInMolecule">
		<g:message code="atom.idInMolecule.label" default="Id In Molecule" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="idInMolecule" required="" value="${atomInstance?.idInMolecule}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: atomInstance, field: 'molecule', 'error')} required">
	<label for="molecule">
		<g:message code="atom.molecule.label" default="Molecule" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="molecule" name="molecule.id" from="${reaktor.Molecule.list()}" optionKey="id" required="" value="${atomInstance?.molecule?.id}" class="many-to-one"/>

</div>

<div class="fieldcontain ${hasErrors(bean: atomInstance, field: 'xCoord', 'error')} required">
	<label for="xCoord">
		<g:message code="atom.xCoord.label" default="X Coord" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="xCoord" value="${fieldValue(bean: atomInstance, field: 'xCoord')}" required=""/>

</div>

<div class="fieldcontain ${hasErrors(bean: atomInstance, field: 'yCoord', 'error')} required">
	<label for="yCoord">
		<g:message code="atom.yCoord.label" default="Y Coord" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="yCoord" value="${fieldValue(bean: atomInstance, field: 'yCoord')}" required=""/>

</div>

<div class="fieldcontain ${hasErrors(bean: atomInstance, field: 'zCoord', 'error')} required">
	<label for="zCoord">
		<g:message code="atom.zCoord.label" default="Z Coord" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="zCoord" value="${fieldValue(bean: atomInstance, field: 'zCoord')}" required=""/>

</div>

