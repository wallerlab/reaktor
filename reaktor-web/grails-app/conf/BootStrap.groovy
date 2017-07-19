import reaktor.*
import reaktor.security.*
import grails.util.Environment

class BootStrap {

	def init = { servletContext ->
		
		def testUser = new User(username: 'max', password: 'password', email: "test@gmail.com")
		testUser.save(flush: true)
			
		def mentorUser = new User(username: 'mentor', password: 'password', email: "mentor@gmail.com")
		mentorUser.save(flush: true)
		
		if(Environment.current == Environment.DEVELOPMENT){
			def adminRole = new Role(authority:'ROLE_ADMIN').save(flush: true)
			def userRole = new Role(authority:'ROLE_USER').save(flush: true)

			UserRole.create(testUser, adminRole, true)
			UserRole.create(mentorUser, userRole, true)
		}

		if(Environment.current == Environment.TEST){

			Reaction reaction = new Reaction(user: testUser, status:
					"calculating", email: "bill@fake.com", reactionType:
					"Reaction", reactionFolderName: "ReactionData_1")
			reaction.save(flush: true)

			Molecule methane = new Molecule(name: "Methane", smilesString:"C")
			Atom carbon = new Atom(element: 'C', xCoord : 0.32, yCoord : 1.2, zCoord : -0.15, idInMolecule: "a1")
			Atom hydrogen1 = new Atom(element: 'H', xCoord : 0.456, yCoord : 1.27, zCoord : 1.35, idInMolecule: "a2")
			Atom hydrogen2 = new Atom(element: 'H', xCoord : 1.2345, yCoord : 1.4, zCoord : 2.43, idInMolecule: "a3")
			Atom hydrogen3 = new Atom(element: 'H', xCoord : 2, yCoord : 0.30000, zCoord : 0.98, idInMolecule: "a4")
			Atom hydrogen4 = new Atom(element: 'H', xCoord : 0.7, yCoord : -1.69, zCoord : -1.2, idInMolecule: "a5")
			methane.addToAtoms(carbon)
			methane.addToAtoms(hydrogen1)
			methane.addToAtoms(hydrogen2)
			methane.addToAtoms(hydrogen3)
			methane.addToAtoms(hydrogen4)
			methane.createElementMap()

			if (!methane.save()) {
				methane.errors.allErrors.each{error -> println "An error occurred with methane: ${error}" }
			}
			if (!carbon.save()) {
				carbon.errors.allErrors.each{error -> println "An error occurred with carbon: ${error}" }
			}

			Molecule dummyMol = new Molecule(name:"dummy", smilesString: "dummy")
			Atom dummyAtom = new Atom(element: "C", xCoord : 1.2345, yCoord : 1.2345, zCoord : 1.2345, idInMolecule: "a1")
			dummyMol.addToAtoms(dummyAtom)
			dummyMol.createElementMap()
			Molecule dummyMol2 = new Molecule(name:"dummy2", smilesString: "dummy2")
			Atom dummyAtom2 = new Atom(element: "C", xCoord : 2.3456, yCoord : 3.456, zCoord : 7.128, idInMolecule: "a1")
			Atom dummyAtom3 = new Atom(element: "H", xCoord : 0.185, yCoord : 3.456, zCoord : 7.128, idInMolecule: "a2")
			Atom dummyAtom4 = new Atom(element: "H", xCoord : 3.289, yCoord : 3.456, zCoord : 7.128, idInMolecule: "a3")
			Atom dummyAtom5 = new Atom(element: "H", xCoord : 9.126, yCoord : 3.456, zCoord : 7.128, idInMolecule: "a4")
			dummyMol2.addToAtoms(dummyAtom2)
			dummyMol2.addToAtoms(dummyAtom3)
			dummyMol2.addToAtoms(dummyAtom4)
			dummyMol2.addToAtoms(dummyAtom5)
			dummyMol2.createElementMap()

			reaction.addToReactants(dummyMol2)
			dummyMol2.setReactantReaction(reaction)

			if (!dummyMol.save()) {
				dummyMol.errors.allErrors.each{error -> println "An error occurred with dummyMol: ${error}" }
			}
			if (!dummyAtom.save()) {
				dummyAtom.errors.allErrors.each{error -> println "An error occurred with dummyAtom: ${error}" }
			}
			if (!dummyMol2.save()) {
				dummyMol2.errors.allErrors.each{error -> println "An error occurred with dummyMol2: ${error}" }
			}
			if (!dummyAtom2.save()) {
				dummyAtom2.errors.allErrors.each{error -> println "An error occurred with dummyAtom2: ${error}" }
			}
			if (!dummyAtom3.save()) {
				dummyAtom3.errors.allErrors.each{error -> println "An error occurred with dummyAtom3: ${error}" }
			}
			if (!dummyAtom4.save()) {
				dummyAtom4.errors.allErrors.each{error -> println "An error occurred with dummyAtom4: ${error}" }
			}
			if (!dummyAtom5.save()) {
				dummyAtom5.errors.allErrors.each{error -> println "An error occurred with dummyAtom5: ${error}" }
			}

			if (!reaction.save()) {
				reaction.errors.allErrors.each{error -> println "An error occurred with reaction: ${error}" }
			}
		}
	}
	def destroy = {
	}
}
