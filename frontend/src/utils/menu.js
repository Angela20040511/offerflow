export function flatMenus(items = []) {
  return items.flatMap((item) => [item, ...(item.children || [])])
}
