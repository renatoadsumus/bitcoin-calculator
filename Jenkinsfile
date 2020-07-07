pipeline {
    
	agent {
	    node{
	        label 'master'
	    }	    
	}
	

	stages{  	
	
		stage('Unit Test'){
		
			steps{					
				
                sh("""docker run \
				 	--rm \
				 	-w /root/codigo_aplicacao \
				 	-v ${WORKSPACE}:/root/codigo_aplicacao \
					-v /home/ec2-user/repositorio:/root/.m2/repository \
				 	renatoadsumus/android:latest gradle clean test
				 """)

				echo "#####################################"	
				echo "###  UNIT TEST ###"
				echo "#####################################"
			}
		}
		
		stage('Build'){
		
			steps{								
				
                sh("""docker run \
				 	--rm \
				 	-w /root/codigo_aplicacao \
					-v /home/ec2-user/repositorio:/root/.m2/repository \
				 	-v ${WORKSPACE}:/root/codigo_aplicacao \
				 	renatoadsumus/android:latest gradle build
				""")

                echo "#####################################"	
                echo "###  BUILD  ###"
                echo "#####################################"                
			
				}		
		}		
		
	
		
		
		stage('Package - Create APK'){
		
			steps{	
				
                sh("""docker run \
				 	--rm \
				 	-w /root/codigo_aplicacao \
				 	-v ${WORKSPACE}:/root/codigo_aplicacao \
					-v /home/ec2-user/repositorio:/root/.m2/repository \
				 	renatoadsumus/android:latest gradle assembleDebug
				""")

                sh("ls ${WORKSPACE}/app/build/outputs/apk/debug/")

				echo "#####################################"
				echo "CREATE APK FILE"		
				echo "#####################################"
			}		
		}

        stage('Functional Test - Device Farm'){
		
			steps{	

				/*dir('appium_test') {          
                    git branch: 'appcenter',
                    credentialsId: 'ff2958ae-9c71-4d8e-997e-9badb8538d9a', 
                    url: 'https://github.com/renatoadsumus/appium.git'	
                

					 sh("""docker run \
						--rm \
						-w /root/codigo_teste \
						-v /home/ec2-user/repositorio:/root/.m2/repository \
						-v ${WORKSPACE}:/root/codigo_aplicacao \
						-v ${WORKSPACE}/appium_test:/root/codigo_teste \
						appium:2.0 mvn -DskipTests -P prepare-for-upload package
					""")

					withCredentials([string(credentialsId: 'TOKEN_APPCENTER', variable: 'TOKEN')]) {
						sh("""docker run \
							--rm \
							-w /root/codigo_teste \
							-e TOKEN_APPCENTER=$TOKEN \
							-v ${WORKSPACE}:/root/codigo_aplicacao \
							-v ${WORKSPACE}/appium_test:/root/codigo_teste \
							appium:2.0 /root/appcenter_run_test.sh
						""")
					}

				}*/

				echo "#####################################"
				echo "FUNCTIONAL TEST DEVICE FARM"		
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
					-e APPCENTER_NAME_APP=renatoadsumus/Bitcoin-Calculator \
					-e APPCENTER_PATH_APK=app/build/outputs/apk/debug/app-debug.apk \
					-e APPCENTER_TOKEN=$TOKEN \
				 	renatoadsumus/android:latest /opt/appcenter_deploy.sh
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

