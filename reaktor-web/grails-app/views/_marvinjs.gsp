<iframe id="sketch" src="${request.contextPath}/js/marvinjs/editor.html"
        style="overflow: hidden; min-width: 500px; min-height:
        450px; border: 1px solid darkgray;"></iframe>


<script>
    var marvinSketcherInstance;
    $(document).ready(function handleDocumentReady (e) {
        MarvinJSUtil.getEditor("#sketch").then(function (marvinPackage) {
            marvinSketcherInstance = marvinPackage;
            initControl();
        }, function (error) {
            alert("Marvin package is not available:" + error);
        });
    });

    function initControl(){
        $("#reaction").click(function () {
            post("Reaction");
        });
        $("#aggregation").click(function () {
            post("Aggregation");
        });
    }

    function post(query_type){
        marvinSketcherInstance.exportStructure("mrv").then(function(source) {
            $.post(
                "${createLink(controller: 'calculate',action: 'startCalculation')}",
                {MolTxt: source, query_type: query_type},
                function(data){
                    handleData(data);
                }
            );
        }, function(error) {
            alert("Molecule export failed:"+error);
        });
    }

    function handleData(data){
        var col=document.createElement("div");
        col.className="col-md-12";
        var box=document.createElement("div");
        box.className="box text-center";
        var boxCont=document.createElement("div");
        boxCont.className="box-content padded";
        var msg=document.createElement("div");
        var text=document.createTextNode(" "+data);
        msg.className="message";
        msg.id="message";
        msg.setAttribute("role", "status");
        var icon = document.createElement("i");
        if(data != "Your calculation has been added to the queue") {
            msg.setAttribute("style", "color:red");
            icon.className = "fa fa-exclamation-triangle";
        } else{
            msg.setAttribute("style", "color:green");
            icon.className = "fa fa-check-square";
        }
        var messageDiv = document.getElementById("messageDiv");
        if(messageDiv.childNodes.length > 0){
            messageDiv.removeChild(messageDiv.childNodes[0]);
        }
        messageDiv.appendChild(col);
        col.appendChild(box);
        box.appendChild(boxCont);
        boxCont.appendChild(msg);
        msg.appendChild(icon);
        msg.appendChild(text);
        $('#example').dataTable().fnDestroy();
        loadTable();

    }
</script>