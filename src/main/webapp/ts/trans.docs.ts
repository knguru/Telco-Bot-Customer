/**
 * (C) Copyright IBM Corp. 2016. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
import {Component} from 'angular2/core';

/*
 * This component is responsible for the Transaction Section layout under Expand/Collapse button.
 * Also contains the modal layout with detailed information
 */

@Component ({
  inputs: ['doc', 'body'],
  selector: 'trans-doc',
  template: `
  <table border="1" width="350">
  <tr>
   <td width="20%">{{getDate()}}</td>
   <td width="20%">{{getType()}}</td>
   <td width="40%">{{getDesc()}}</td>
   <td width="20%">{{getAmt()}}</td>
  </tr>
  </table>
`
})

export class TransDocComponent {
  private doc : any;
  private isExpand : boolean = true;
  private isLoading : boolean;
  private heighSet : boolean = false;

  private toggle (newval) {
    this.isExpand = newval;
  }

  private getDate () {
    if (this.doc) {
      return this.doc.date;
    }
    return '';
  }

  private getType () {
    if (this.doc) {
      return this.doc.type;
    }
    return '';
  }
  private getDesc () {
    if (this.doc) {
      return this.doc.desc;
    }
    return '';
  }
  private getAmt () {
    if (this.doc) {
      return this.doc.amount;
    }
    return '';
  }
}
