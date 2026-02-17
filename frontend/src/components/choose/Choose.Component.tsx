interface ChooseProps<T> {
  label?: string;
  value: T;
  options: T[];
  onChange: (value: T) => void;
}

function Choose<T extends string>({
  label,
  value,
  options,
  onChange,
}: ChooseProps<T>) {
  return (
    <div>
      {label && <p>{label}</p>}
      <select value={value} onChange={(e) => onChange(e.target.value as T)}>
        {options.map((option) => (
          <option key={option} value={option}>
            {option}
          </option>
        ))}
      </select>
    </div>
  );
}

export default Choose;
