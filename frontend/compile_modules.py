import os
import os.path
import sys
import subprocess
import re
import shutil

def buildModule(name, version, arguments=[]):
    cwd  = os.getcwd()
    os.chdir(name)

    sys.stdout.write('Building module ' + name + '... ')

    args = ['mvn', '-Dmaven.test.skip=true']
    if arguments:
        args += arguments
    args += ['install']

    process = subprocess.Popen(args, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
    output = process.communicate()[0]
    rc = process.returncode

    expectedFile = 'target/' + name + '-' + version + '.jar'

    if rc != 0 and not os.path.isfile(expectedFile):
        sys.stdout.write('FAILED! Maven error log:\n' +  output)
    else:
        shutil.copy2(expectedFile, '../../lib/' + name + '.jar')
        sys.stdout.write('done!\n')
    os.chdir(cwd)



if not os.path.exists('lib'):
    os.makedirs('lib')
os.chdir('modules')

buildModule('DBModule', '1.0')
buildModule('AuthModule', '1.0', ['install:install-file',
                                    '-Dfile=../DBModule/target/DBModule-1.0.jar',
                                    '-DgroupId=be.ugent.edran',
                                    '-DartifactId=DBModule',
                                    '-Dversion=1.0',
                                    '-Dpackaging=jar'])
