echo "Version: 1.0"
echo '##################'

echo 'Script started'
echo ''

# Build and publish docker image versions latest and 1.0 for each service

# library-monolith
echo '  ******* '
echo '* LIBRARY MONOLITH *'
echo '  ******* '
cd Practica2_Java_v2
echo 'Building library-monolith image...'
docker build -f Dockerfile -t juablazmahuerta/juablazmahuerta/library-monolith .
echo 'library-monolith image built'
cd ..
docker tag juablazmahuerta/juablazmahuerta/library-monolith juablazmahuerta/library-monolith:0.0.1
echo 'Pushing library-monolith image to dockerhub'
docker push juablazmahuerta/library-monolith
docker push juablazmahuerta/library-monolith:0.0.1
echo 'library-monolith images published in dockerhub'
echo ''

# userms
echo '  ******* '
echo '* USER MS *'
echo '  ******* '
cd user-ms
echo 'Building user-ms image...'
docker build -f Dockerfile -t juablazmahuerta/juablazmahuerta/user-ms .
echo 'user-ms image built'
cd ..
docker tag juablazmahuerta/juablazmahuerta/user-ms juablazmahuerta/user-ms:0.0.1
echo 'Pushing user-ms image to dockerhub'
docker push juablazmahuerta/user-ms
docker push juablazmahuerta/user-ms:0.0.1
echo 'user-ms images published in dockerhub'
echo ''