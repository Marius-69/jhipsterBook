import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'book',
        data: { pageTitle: 'jhipsterBookApp.book.home.title' },
        loadChildren: () => import('./book/book.routes'),
      },
      {
        path: 'author',
        data: { pageTitle: 'jhipsterBookApp.author.home.title' },
        loadChildren: () => import('./author/author.routes'),
      },
      {
        path: 'edition',
        data: { pageTitle: 'jhipsterBookApp.edition.home.title' },
        loadChildren: () => import('./edition/edition.routes'),
      },
      {
        path: 'style',
        data: { pageTitle: 'jhipsterBookApp.style.home.title' },
        loadChildren: () => import('./style/style.routes'),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
