import { TestBed, inject } from '@angular/core/testing';
import { } from 'jasmine';
import { CamerasService } from './cameras.service';

describe('CamerasService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CamerasService]
    });
  });

  it('should be created', inject([CamerasService], (service: CamerasService) => {
    expect(service).toBeTruthy();
  }));
});
