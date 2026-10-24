/** An experiment as returned by the backend API. */
export interface Experiment {
  id: string;
  name: string;
  sequences: string[];
  createdAt: string;
}
