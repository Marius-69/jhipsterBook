import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IEdition } from '../edition.model';
import { EditionService } from '../service/edition.service';

@Component({
  standalone: true,
  templateUrl: './edition-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class EditionDeleteDialogComponent {
  edition?: IEdition;

  constructor(
    protected editionService: EditionService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.editionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
