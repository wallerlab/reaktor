package reaktor.uploadr

import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN','ROLE_USER'])
class UploadController extends hungry.wombat.UploadController{
}
