pipeline {
    
	agent {
	    node{
	        label 'master'
	    }	    
	}
	

	stages{  
	
		stage('Build'){
		
			steps{								
				
                sh("""docker run \
				 	--rm \
				 	-w /root/codigo_aplicacao \
				 	-v ${WORKSPACE}:/root/codigo_aplicacao \
				 	appium:2.0 gradlew build
				""")

                echo "#####################################"	
                echo "###  BUILD  ###"
                echo "#####################################"                
			
				}		
		}		
		
		stage('Unit Test'){
		
			steps{					
				
                sh("""docker run \
				 	--rm \
				 	-w /root/codigo_aplicacao \
				 	-v ${WORKSPACE}:/root/codigo_aplicacao \
				 	appium:2.0 gradlew test
				 """)

				echo "#####################################"	
				echo "###  UNIT TEST ###"
				echo "#####################################"
			}
		}
		
		
		stage('Package - Create APK'){
		
			steps{	
				
                sh("""docker run \
				 	--rm \
				 	-w /root/codigo_aplicacao \
				 	-v ${WORKSPACE}:/root/codigo_aplicacao \
				 	appium:2.0 gradlew assembleDebug
				""")

                sh("ls ${WORKSPACE}/app/build/outputs/apk/debug/")

				echo "#####################################"
				echo "CREATE APK FILE"		
				echo "#####################################"
			}		
		}

        stage('Functional Test - Device Farm'){
		
			steps{	

				dir('appium_test') {          
                    git branch: 'master',
                    credentialsId: 'ff2958ae-9c71-4d8e-997e-9badb8538d9a', 
                    url: 'https://github.com/renatoadsumus/appium.git'	
                

				/* sh("""docker run \
				 	--rm \
				 	-w /root/codigo_aplicacao \
				 	-v ${WORKSPACE}:/root/codigo_aplicacao \
				 	appium:2.0 mvn -DskipTests -P prepare-for-upload package
				""")*/

				}

				echo "#####################################"
				echo "FUNCTIONAL TEST DEVIVE FARM"		
				echo "#####################################"
			}		
		}

         stage('Deploy AppCenter'){
		
			steps{	

				withCredentials([string(credentialsId: 'TOKEN_APPCENTER', variable: 'TOKEN')]) {
					sh("""docker run \
				 	--rm \
				 	-w /root/codigo_aplicacao \
				 	-v ${WORKSPACE}:/root/codigo_aplicacao \
					-e TOKEN_APPCENTER=$TOKEN
				 	appium:2.0 /root/appcenter_deploy.sh
				""")		
			
				}

				echo "#####################################"
				echo "DEPLOY APPCENTER"		
				echo "#####################################"
			}		
		}
	}

	post {
        always {   
		  //cleanWs()
          echo "Eliminando..."
        }
    }	
}

