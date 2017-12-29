import { IntrusionDetectorPage } from './app.po';

describe('intrusion-detector App', () => {
  let page: IntrusionDetectorPage;

  beforeEach(() => {
    page = new IntrusionDetectorPage();
  });

  it('should display welcome message', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('Welcome to app!!');
  });
});
