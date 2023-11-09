import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IStyleDeux } from '../style-deux.model';
import { StyleDeuxService } from '../service/style-deux.service';

@Component({
  standalone: true,
  templateUrl: './style-deux-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class StyleDeuxDeleteDialogComponent {
  styleDeux?: IStyleDeux;

  constructor(
    protected styleDeuxService: StyleDeuxService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.styleDeuxService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
