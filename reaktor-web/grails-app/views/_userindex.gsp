<div class="box">
	<div class="box-header">
		<span class="title">Your Reactions</span>
	</div>
	<div class="box-content padded scrollable text-center" style="height: 552px; overflow-y: auto">
		<script type="text/javascript" class="init">
			$(document).ready(function() {
				$('#example').DataTable({
					"paging":   false,
					"filter": false,
					"info": false,
		        	"ajax": "${createLink(controller:'reaction',action: 'getUserIndex')}",		       
		        	"columns": [	  
		    			{ "data": "id"},      
		            	{ "data": "molecules", "render": "[, ].molecule.name" },
		 		    	{ "data": "status" },
		            	{ "data": "lastUpdated" }           
		        	],
		        	"columnDefs": [
		   		     	{
		   		     		"render": function (data, display, row) {
		   			    		var displayData = "<a href='reaction/show/"+data+"' class='btn btn-sm btn-info'>"+data+"</a>";
		   		        		return displayData;
		   		        	},
		   		        	"targets": 0
		   		      	}]   
		    	});

				$('#example tbody').on('click', 'tr', function() {
					var reactionID = $('td', this).eq(0).text();
					loadmol(reactionID);
				});

			});
		</script>
	
		<script type="text/javascript" src="${request.contextPath}/js/ChemDoodleWeb.js"></script>
		<script>
			var transformLine = new ChemDoodle.TransformCanvas3D('transformLine', 350, 250);
			transformLine.specs.set3DRepresentation('Line');
			transformLine.specs.backgroundColor = 'black';

			function loadmol(reactionID){
				jQuery.ajax({
		   			url: "${createLink(controller: 'calculate', action: 'getReactants')}",
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
	
		<table id="example" class="dTable responsive">
			<thead>
				<tr>
					<th>ID</th>
					<th>Reactants</th>
					<th>Status</th>
					<th>Last Updated</th>
				</tr>
			</thead>
		</table>

	
		<!-- DataTables -->
		<script type="text/javascript" charset="utf8" src="//cdn.datatables.net/1.10.3/js/jquery.dataTables.js"></script>
    
    	<!-- datatables bootstrap -->
		<script type="text/javascript" charset="utf8" src="//cdn.datatables.net/plug-ins/a5734b29083/integration/bootstrap/3/dataTables.bootstrap.js"></script>
	
	</div>
</div>
