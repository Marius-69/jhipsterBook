import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { BookDetailComponent } from './book-detail.component';

describe('Book Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BookDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: BookDetailComponent,
              resolve: { book: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(BookDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load book on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', BookDetailComponent);

      // THEN
      expect(instance.book).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
