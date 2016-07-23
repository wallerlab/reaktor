# *[`http://reaktor.wallerlab.org`](http://reaktor.wallerlab.org)*

[![Apache License](http://img.shields.io/badge/license-APACHE2-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![Build Status](https://travis-ci.org/wallerlab/reaktor.svg?branch=master)](https://travis-ci.org/wallerlab/reaktor)
[![Coverage Status](https://coveralls.io/repos/github/wallerlab/reaktor/badge.svg?branch=master)](https://coveralls.io/github/wallerlab/reaktor?branch=master)

A web app that predicts the possible products of a binary reaction by carrying out quantum chemical calculations in the gas-phase.

![reaktor2](https://cloud.githubusercontent.com/assets/13583117/16518932/4c871e74-3fb8-11e6-8859-c092a6d1ec7b.png)

Reaktor needs inputs from:

1. Two user-uploaded .xyz files
2. or two user-drawn molecules
3. or from in-house queues


Reaktor creates a workspace environment on a compute cluster, and sends DFT calculations to that cluster. 

Results are polled by Reaktor, and a database is used to store the reactants, products, and reaction energies.

Reaktor can then be used to visualize the products in a 3D interactive viewer.

Reaktor uses the pyReactor code which was written by S. Nandi and A. Ayyappan from the IIT in Kharagpur.
https://bitbucket.org/anoopmvpa/pyreactor


#Build

`git clone https://github.com/wallerlab/reaktor.git `

`cd reaktor`

Cluster

`./reacktor-cluster/gradlew clean build`

`java -jar build/libs/reaktor-cluster-0.1-SNAPSHOT.jar`

Web

`./reacktor-web/grailsw run-app`

