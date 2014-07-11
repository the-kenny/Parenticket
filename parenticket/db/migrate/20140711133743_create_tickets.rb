class CreateTickets < ActiveRecord::Migration
  def change
    create_table :tickets do |t|
      t.text :name
      t.text :description
      t.integer :status
      t.integer :priority
      t.datetime :deadline

      t.timestamps
    end
  end
end
