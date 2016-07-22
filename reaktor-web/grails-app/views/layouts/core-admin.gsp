<!doctype html>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, maximum-scale=1, initial-scale=1, user-scalable=0">

  <!-- Always force latest IE rendering engine or request Chrome Frame -->
  <meta content="IE=edge,chrome=1" http-equiv="X-UA-Compatible">

  <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Open+Sans:400,600,800">

  <!-- Use title if it's in the page YAML frontmatter -->
  <title>Reaktor</title>
  <link rel="shortcut icon" href="${assetPath(src: 'reaction.ico')}" type="image/x-icon">
  <link href="${request.contextPath}/core-admin/release_bs3/build/stylesheets/application.css" media="screen" rel="stylesheet" type="text/css" />
  
  <!-- DataTables CSS -->
  <link rel="stylesheet" href="//cdn.datatables.net/plug-ins/a5734b29083/integration/bootstrap/3/dataTables.bootstrap.css">

  <script src="${request.contextPath}/core-admin/release_bs3/build/javascripts/application.js" type="text/javascript"></script>
  
  <style type="text/css" class="init">
</style>
  
  <asset:javascript src="uploadr.manifest.js"/>
  <asset:stylesheet href="uploadr.manifest.css"/>
</head>



<body>



<nav class="navbar navbar-default navbar-inverse navbar-static-top" role="navigation">
  <!-- Brand and toggle get grouped for better mobile display -->
  <div class="navbar-header">
    <a class="navbar-brand" href="/reaktor">Reaktor</a>

    
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse-primary">
          <span class="sr-only">Toggle Side Navigation</span>
          <i class="icon-th-list"></i>
        </button>

        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse-top">
          <span class="sr-only">Toggle Top Navigation</span>
          <i class="icon-align-justify"></i>
        </button>
    
  </div>

  
      

      <!-- Collect the nav links, forms, and other content for toggling -->
      <div class="collapse navbar-collapse navbar-collapse-top">
        <div class="navbar-right">

          <ul class="nav navbar-nav navbar-left">
          <sec:ifNotLoggedIn>
            <li class="cdrop active"><a href="${createLink(uri:'/login')}">Log in</a></li>

            <li class="cdrop"><a href="${createLink(uri:'/register')}">Register</a></li>
		</sec:ifNotLoggedIn>
		<sec:ifLoggedIn>
			<li class="cdrop active">
			<g:form controller="logout">
				<g:submitButton name="Submit" value="Log out"/>
			</g:form>
			</li>
		</sec:ifLoggedIn>
          </ul>

		<sec:ifLoggedIn>
          <ul class="nav navbar-nav navbar-left">
            <li class="dropdown">
              <a href="#" class="dropdown-toggle dropdown-avatar" data-toggle="dropdown">
              <span>
                <img class="menu-avatar" src="${request.contextPath}/images/reaction.png" /> <span><sec:username/> <i class="icon-caret-down"></i></span>
              </span>
              </a>
              <ul class="dropdown-menu">

                <!-- the first element is the one with the big avatar, add a with-image class to it -->

                <li class="with-image">
                  <div class="avatar">
                    <img src="${request.contextPath}/images/drawMolecule.png" />
                  </div>
                  <span><sec:username/></span>
                </li>

                <li class="divider"></li>

                <li><g:remoteLink class="logout" controller="logout" method="post" asynchronous="false" onSuccess="location.assign('/reaktor/')">
                	<i class="icon-off"></i>Log Out
                	</g:remoteLink></li>
                <li><g:link controller="register" action="changePassword">Reset Password</g:link></li>
              </ul>
            </li>
          </ul>
         </sec:ifLoggedIn>
        </div>




      </div><!-- /.navbar-collapse -->

  
</nav>
<div class="sidebar-background">
  <div class="primary-sidebar-background"></div>
</div>

<div class="primary-sidebar">

  <!-- Main nav -->
  <ul class="nav navbar-collapse collapse navbar-collapse-primary">
    

        <li class="active dark-nav">
          <span class="glow"></span>
          <a class="accordion-toggle collapsed " data-toggle="collapse" href="#yJ6h3Npe7C">
              <i class="icon-beaker icon-2x"></i>
                    <span>
                      Reaktor
                      <%--<i class="icon-caret-down"></i>
                    --%></span>
          </a>
        </li>

  </ul>

    </div>
 
<div class="main-content">
  <div class="container">
    <div class="row">

      <div class="area-top clearfix">
        <div class="pull-left header">
          <h3 class="title">
            <i class="icon-beaker"></i>
            <sec:ifNotLoggedIn>Welcome to Reaktor</sec:ifNotLoggedIn>
            <sec:ifLoggedIn><div class="intro-lead-in">Welcome <sec:username/></div>
            </sec:ifLoggedIn>
          </h3>
          <h5>
            <span>
              You give us reactants, we give you the products!
            </span>
          </h5>
        </div>

      </div>
    </div>
  </div>

  <div class="container padded">
    <div class="row">

      <!-- Breadcrumb line -->

      <div id="breadcrumbs">
        <div class="breadcrumb-button blue">
          <span class="breadcrumb-label"><i class="icon-home"></i> Home</span>
          <span class="breadcrumb-arrow"><span></span></span>
        </div>

        

        <div class="breadcrumb-button">
          <span class="breadcrumb-label">
            <i class="icon-beaker"></i> Reaktor
          </span>
          <span class="breadcrumb-arrow"><span></span></span>
        </div>
      </div>
    </div>
  </div>

  <div class="container">
    <div class="row">
    
    <g:layoutBody/>


</div>
</div>
</div>
</body>
</html>
