import * as chai from 'chai';
import * as request from 'request';
import { db } from './dbConnect';

let token = '';
const url = 'http://localhost:4300/api/v1';
let camera: any;


describe('Cameras', function () {
    before(function(done) {
        db.any('insert into cameras(camera_name , location , online , description) values(\'cam1\', \'(4,4)\', true, \'asgsadg4554\')')
            .then(data => {
                db.any('insert into users( name, role, email, password) \
                values(\'Admin\', \'admin\', \'admin@mail.ru\', \'3ca6ada1-90f3-54a6-85f0-0696a9974fc0\') ')
                    .then(newData => {
                        done();
                    })
                    .catch(err => {
                    })
            })
            .catch(err => {
            })

    })

    it('should return all cameras list', function (done) {
        request.get(url + '/cameras', function (err, res, body){
            chai.expect(res.statusCode).to.equal(200);
            chai.expect(JSON.parse(res.body)[0]).to.have.property('id');
            chai.expect(JSON.parse(res.body)[0]).to.have.property('camera_name');
            chai.expect(JSON.parse(res.body)[0]).to.have.property('location');
            chai.expect(JSON.parse(res.body)[0]).to.have.property('description');
            chai.expect(JSON.parse(res.body)[0]).to.have.property('online');
            camera = JSON.parse(res.body)[0];
            done();
        });
    });

    it('should get error message no authentication delete camera by id', function (done) {
            request.delete(url + '/camera/' + camera.id, {
                form: {
                    cameraName: 'name',
                    latitude: 5,
                    longitude: 5,
                    description: 'description',
                    online: 'true',
                },
                headers: {
                    'Content-Type': 'application/json',
                },
            },
                function (err, res, body){
                chai.expect(res.statusCode).to.equal(401);
                chai.expect(JSON.parse(res.body).success).to.equal(false);
                chai.expect(JSON.parse(res.body).message).to.equal('No authentication token was provided in the request');
                done();
            });
    });

    it('should get error message no authentication update camera by id', function (done) {
            request.put(url + '/camera/' + camera.id, {
                form: {
                    cameraName: 'name',
                    latitude: 5,
                    longitude: 5,
                    description: 'description',
                    online: 'true',
                },
                headers: {
                    'Content-Type': 'application/json',
                }
            }
            , function (err, res, body){
                chai.expect(res.statusCode).to.equal(401);
                chai.expect(JSON.parse(res.body).success).to.equal(false);
                chai.expect(JSON.parse(res.body).message).to.equal('No authentication token was provided in the request');
                done();
            });
    });

    it('should get token login', function (done) {
        request.post(url + '/login'
        , {
            form: {
                userName: 'admin@mail.ru',
                password: 'Admin1',
            },
          }
        , function (err, res, body){
            chai.expect(res.statusCode).to.equal(200);
            token = JSON.parse(JSON.stringify(res.headers))['set-cookie'][0].slice(13, -8);
            done();
        });
    });

    it('Update camera by id', function (done) {
            request.put(url + '/camera/' + camera.id
            , {
                form: {
                    cameraName: 'name',
                    latitude: 5,
                    longitude: 5,
                    description: 'description',
                    online: 'true',
                },
                headers: {
                    'cookie': 'access_token=' + token,
                    'Content-Type': 'application/json',
                },
            }
            , function (err, res, body){
                chai.expect(res.statusCode).to.equal(201);
                done();
            });
    });

    it('Update camera by id get error bad request', function (done) {
            request.put(url + '/camera/' + camera.id
            , {
                form: {
                    cameraName: 'name',
                    latitude: 5,
                    longitude: 'a',
                    description: 'description',
                    online: 'true',
                },
                headers: {
                    access_token: token,
                    'Content-Type': 'application/json',
                },
            }
            , function (err, res, body){
                chai.expect(res.statusCode).to.equal(400);
                done();
            });
    });

    it('Update camera by id get error no token', function (done) {
            request.put(url + '/camera/' + camera.id, {
                form: {
                    cameraName: 'name',
                    latitude: 5,
                    longitude: 5,
                    description: 'description',
                    online: 'true',
                },
                headers: {
                    'Content-Type': 'application/json',
                },
            }
            , function (err, res, body){
                chai.expect(JSON.parse(res.body).success).to.equal(false);
                chai.expect(JSON.parse(res.body).message).to.equal('No authentication token was provided in the request');
                done();
            });
    });

    it('should get error bad request delete camera by id', function (done) {
            request.delete(url + '/camera/a',
            {
                headers: {
                    access_token: token,
                },
            },
            function (err, res, body){
                chai.expect(res.statusCode).to.equal(400);
                done();
            });
    });

    it('should get error bad request delete camera by id', function (done) {
            request.delete(url + '/camera/5.5',
            {
                headers: {
                    access_token: token,
                },
            },
            function (err, res, body){
                chai.expect(res.statusCode).to.equal(400);
                done();
            });
    });

    it('should get error invalid token delete camera by id', function (done) {
            request.delete(url + '/camera/' + camera.id,
            {
                headers: {
                    'cookie': 'access_token=',
                    'Content-Type': 'application/json',
                },
            },
            function (err, res, body){
                chai.expect(res.statusCode).to.equal(401);
                done();
            });
    });

    it('should delete camera by id', function (done) {
            request.delete(url + '/camera/' + camera.id,
            {
                headers: {
                    'cookie': 'access_token=' + token,
                    'Content-Type': 'application/json',
                },
            },
            function (err, res, body){
                chai.expect(res.statusCode).to.equal(201);
                done();
            });
    });
});

