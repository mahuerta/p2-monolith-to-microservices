const { spawnSync } = require('child_process');

function exec(serviceName, command){

  console.log(`Installing dependencies for [${serviceName}]`);
  console.log(`Folder: ${serviceName} Command: ${command}`);

  spawnSync(command, [], { 
    cwd: serviceName,
    shell: true,
    stdio: 'inherit'
  });
}

exec('Practica2_Java_v2', 'npm install -DskipTests');
exec('user-ms', 'mvn install -DskipTests');
