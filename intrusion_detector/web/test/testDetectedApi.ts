import * as chai from 'chai';
import * as request from 'request';
import { db } from './dbConnect';


let token = '';
const url = 'http://localhost:4300/api/v1';
let object: any;


describe('Detected Objects', function () {

    before(function(done) {
        db.any('insert into cameras(camera_name , location , online , description) values(\'cam1\', \'(4,4)\', true, \'asgsadg4554\')')
            .then(data => {
                done();
            })
            .catch(err => {
            })
    })

    it('should insert  Objects', function (done) {
        request.post(url + '/object', {
            form: {
                cameraName: 'cam1',
                type: 'car',
                firstDetectedDate: 1483257780,
            }
        },
        function (err, res, body){
            chai.expect(res.statusCode).to.equal(201);
            chai.expect(JSON.parse(res.body)).to.have.property('code');
            chai.expect(JSON.parse(res.body)).to.have.property('folderName');
            done();
        });
    });

    it('should return all Objects list', function (done) {
        request.get(url + '/objects', function (err, res, body) {
            chai.expect(res.statusCode).to.equal(200);
            chai.expect(JSON.parse(res.body).objects[0]).to.have.property('type');
            chai.expect(JSON.parse(res.body).objects[0]).to.have.property('firstDetectedDate');
            chai.expect(JSON.parse(res.body).objects[0]).to.have.property('cameras');
            chai.expect(JSON.parse(res.body).objects[0]).to.have.property('images');
            chai.expect(JSON.parse(res.body).objects[0]).to.have.property('id');
            chai.expect(JSON.parse(res.body).objects[0]).to.have.property('imagesEncode');
            object = JSON.parse(res.body).objects[0];
            done();
        });
    });

    it('should get token login', function (done) {
        request.post(url + '/login', {
            form: {
                userName: 'admin@mail.ru',
                password: 'Admin1',
            },
        },
        function (err, res, body){
            chai.expect(res.statusCode).to.equal(200);
            token = JSON.parse(JSON.stringify(res.headers))['set-cookie'][0].slice(13, -8);
            done();
        });
    });

     it('Update object by id', function (done) {
        request.put(url + '/object/' + object.id, {
            form: {
               'type': 'car',
               'first_detected_date' : '2017-08-08 14:30:10'
            },
            headers: {
                'Content-Type': 'application/json',
                'cookie': 'access_token=' + token,
            }
        },
        function (err, res, body){
            chai.expect(res.statusCode).to.equal(201);
            done();
        })
    });


    it('Update object by id get error bad request', function (done) {
        request.put(url + '/object/h', {
            form: {
                'type': 'car',
                'first_detected_date' : '2017-dfhsfh-08 14:30:10',
            },
            headers: {
                'cookie': 'access_token=' + token,
                'Content-Type': 'application/json',
            },
        },
        function (err, res, body){
            chai.expect(res.statusCode).to.equal(400);
            done();
        });
    });

    it('should get error message no authentication update object by id', function (done) {
        request.put(url + '/object/' + object.id, {
            form: {
                'type': 'car',
                'first_detected_date' : '2017-08-08 14:30:10',
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

    it('should get error message no authentication delete object by id', function (done) {
        request.delete(url + '/object/' + object.id,
        function (err, res, body){
            chai.expect(res.statusCode).to.equal(401);
            chai.expect(JSON.parse(res.body).success).to.equal(false);
            chai.expect(JSON.parse(res.body).message).to.equal('No authentication token was provided in the request');
            done();
        });
    });

    it('should get error bad request delete object by id', function (done) {
        request.delete(url + '/object/a', {
            headers: {
                'cookie': 'access_token=' + token,
            },
        },
        function (err, res, body){
            chai.expect(res.statusCode).to.equal(400);
            done();
        });
    });

    it('should get error bad request delete object by id', function (done) {
        request.delete(url + '/object/5.5', {
            headers: {
                'cookie': 'access_token=' + token,
            },
        },
        function (err, res, body){
            chai.expect(res.statusCode).to.equal(400);
            done();
        });
    });

    it('should get error invalid token delete object by id', function (done) {
        request.delete(url + '/object/' + object.id, {
            headers: {
                'cookie': 'access_token=',
            },
        },
        function (err, res, body){
            chai.expect(res.statusCode).to.equal(401);
            done();
        });
    });

    it('should delete object by id', function (done) {
        request.delete(url + '/object/' + object.id, {
            headers: {
                'cookie': 'access_token=' + token,
            },
        },
        function (err, res, body){
            chai.expect(res.statusCode).to.equal(201);
            done();
        });
    });
});
