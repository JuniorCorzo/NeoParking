export const rateToPercentage = (rate?: number | null): number => {
  if (rate === null || rate === undefined || !Number.isFinite(rate) || rate <= 0) {
    return 0;
  }
  return Math.round(rate * 100 * 10000) / 10000;
};

export const percentageToRate = (percentage?: number | null): number => {
  if (
    percentage === null ||
    percentage === undefined ||
    !Number.isFinite(percentage) ||
    percentage <= 0
  ) {
    return 0;
  }
  return Math.round((percentage / 100) * 10000) / 10000;
};

export const PercentageConverter = {
  rateToPercentage,
  percentageToRate,
} as const;
