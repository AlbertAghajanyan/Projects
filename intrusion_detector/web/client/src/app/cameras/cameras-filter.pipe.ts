import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'camerasFilter'
})

export class CamerasFilterPipe implements PipeTransform {

  private data: any[];

  transform(items: any[], name: string, lat: number, long: number): any[] {
   this.data = items;
    if (!items) {
      return [];
    } else if (!name && !lat && !long) {
      return items;
    }
    if (name) {
      return this.searchCameraByName(items, name);
    } else {
      return this.searchCameraByLocation(items, lat, long);
    }
  };

  /**
   * Search camera by name
   * @param obj camera field
   * @param cameraName camera name string
   */
  searchCameraByName(obj, cameraName) {
    const items = [];
    for (let i = 0, j = 0; i < obj.length; ++i) {
      if (this.checkName(obj[i].camera_name, cameraName)) {
        items[j] = obj[i];
        j++;
      }
    }
    return items;
  }

  /**
   * Check name for a match
   *
   */
  checkName(cameraName: string, searchName: string) {
    let check = true;
    for (let i = 0; i < searchName.length; ++i) {
      if (cameraName[i] !== searchName[i]) {
        check = false;
      }
    }
    return check;
  }

  /**
   * Search camera by latitude and longitude
   *
   * @param obj camera field
   * @param lat latitude
   * @param long longitude
   */
  searchCameraByLocation(obj, lat, long) {
    const items = [];
    if (lat !== '' && long !== '') {
      for (let i = 0, j = 0; i < obj.length; ++i) {
        const resLat = ((obj[i].location.x - lat) > 0) ? (obj[i].location.x - lat) : -(obj[i].location.x - lat)
        const resLong = ((obj[i].location.y - long) > 0) ? (obj[i].location.y - long) : -(obj[i].location.y - long)
        if (resLat <= 0.5 && resLong <= 0.5) {
          items[j] = obj[i];
          j++;
        }
      }
    } else if ('' === long) {
      for (let i = 0, j = 0; i < obj.length; ++i) {
        const res = ((obj[i].location.x - lat) > 0) ? (obj[i].location.x - lat) : -(obj[i].location.x - lat)
        if (res <= 0.5) {
          items[j] = obj[i];
          j++;
        }
      }
    } else if ('' === lat) {
        for (let i = 0, j = 0; i < obj.length; ++i) {
          const res = ((obj[i].location.y - long) > 0) ? (obj[i].location.y - long) : -(obj[i].location.y - long)
          if (res <= 0.5) {
            items[j] = obj[i];
            j++;
          }
        }
    }
    return items;
  }
}

