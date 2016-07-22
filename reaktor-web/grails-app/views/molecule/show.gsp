<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="core-admin" />
	<link rel="stylesheet" href="${request.contextPath}/css/ChemDoodleWeb.css" type="text/css">
	<style type="text/css" media="screen">
		.center {
		margin: 20px;
		}
	</style>
</head>
<body>

	<div class="col-md-12">
	<div class="box">
      <div class="box-header">
        <span class="title">${name }</span>
      </div>
      <div class="box-content padded scrollable text-center" style="height: 552px; overflow-y: auto">

		<script type="text/javascript" SRC="${request.contextPath}/js/ChemDoodleWeb.js"></script>
		<script type="text/javascript">
			var viewer=new ChemDoodle.TransformCanvas3D("viewer",500,500);
			viewer.specs.set3DRepresentation('Ball and Stick');
			viewer.specs.backgroundColor = 'black';
			viewer.specs.atoms_useJMOLColors = true;
			var mol=ChemDoodle.readXYZ("${xyzFileString}");
			viewer.loadMolecule(mol)
		</script>

	</div>
	</div>
	</div>
</body>
</html>